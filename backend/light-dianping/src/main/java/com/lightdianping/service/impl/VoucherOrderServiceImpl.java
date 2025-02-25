package com.lightdianping.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lightdianping.dto.Result;
import com.lightdianping.entity.VoucherOrder;
import com.lightdianping.mapper.VoucherOrderMapper;
import com.lightdianping.service.ISeckillVoucherService;
import com.lightdianping.service.IVoucherOrderService;
import com.lightdianping.utils.RedisIdWorker;
import com.lightdianping.utils.UserHolder;
import jakarta.annotation.PostConstruct;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {

    @Autowired
    private ISeckillVoucherService seckillVoucherService;

    @Autowired
    private RedisIdWorker redisIdWorker;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    private IVoucherOrderService proxy;

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;
    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    private static final ExecutorService SECKILL_ORDER_EXECUTOR = Executors.newSingleThreadExecutor();

    @PostConstruct
    private void init() {
        SECKILL_ORDER_EXECUTOR.submit(new VoucherOrderHandler());
    }

    private class VoucherOrderHandler implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
                            Consumer.from("g1", "c1"),
                            StreamReadOptions.empty().count(1).block(Duration.ofSeconds(2)),
                            StreamOffset.create("stream.orders", ReadOffset.lastConsumed())
                    );

                    if (list == null || list.isEmpty()) {
                        continue;
                    }

                    MapRecord<String, Object, Object> record = list.get(0);
                    Map<Object, Object> values = record.getValue();
                    VoucherOrder order = BeanUtil.fillBeanWithMap(values, new VoucherOrder(), true);

                    handleVoucherOrder(order);

                    stringRedisTemplate.opsForStream().acknowledge("stream.orders", "g1", record.getId());

                } catch (Exception e) {
                    log.error("处理订单异常", e);
                    handlePendingList();
                }
            }
        }

        private void handlePendingList() {
            while (true) {
                try {
                    List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
                            Consumer.from("g1", "c1"),
                            StreamReadOptions.empty().count(1),
                            StreamOffset.create("stream.orders", ReadOffset.from("0"))
                    );

                    if (list == null || list.isEmpty()) {
                        break;
                    }

                    MapRecord<String, Object, Object> record = list.get(0);
                    Map<Object, Object> values = record.getValue();
                    VoucherOrder order = BeanUtil.fillBeanWithMap(values, new VoucherOrder(), true);

                    handleVoucherOrder(order);

                    stringRedisTemplate.opsForStream().acknowledge("stream.orders", "g1", record.getId());

                } catch (Exception e) {
                    log.error("处理pending-list异常", e);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }

    private void handleVoucherOrder(VoucherOrder order) {
        Long userId = order.getUserId();
        RLock lock = redissonClient.getLock("lock:order:" + userId);
        boolean isLock = lock.tryLock();

        if (!isLock) {
            log.error("不允许重复下单");
            return;
        }

        try {
            proxy.createVoucherOrder(order);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Result seckillVoucher(Long voucherId) {
        Long userId = UserHolder.getUser().getId();
        long orderId = redisIdWorker.nextId("order");

        Long result = stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                voucherId.toString(), userId.toString(), String.valueOf(orderId)
        );

        int r = result.intValue();
        if (r != 0) {
            return Result.fail(r == 1 ? "库存不足" : "每人限购一张");
        }

        proxy = (IVoucherOrderService) AopContext.currentProxy();

        return Result.ok(orderId);
    }

    @Transactional
    public void createVoucherOrder(VoucherOrder order) {
        // 一人一单
        Long userId = order.getUserId();

        Long count = query().eq("user_id", userId).eq("voucher_id", order.getVoucherId()).count();

        if (count > 0) {
            log.error("每人限购一张");
            return;
        }

        // 乐观锁更新库存
        boolean success = seckillVoucherService.update()
                .setSql("stock = stock - 1")
                .eq("voucher_id", order.getVoucherId()).gt("stock", 0)
                .update();

        if (!success) {
            log.error("库存不足");
            return;
        }

        save(order);
    }
}
