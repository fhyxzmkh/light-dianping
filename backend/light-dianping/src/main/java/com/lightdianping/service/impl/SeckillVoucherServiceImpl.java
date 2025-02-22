package com.lightdianping.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lightdianping.entity.SeckillVoucher;
import com.lightdianping.mapper.SeckillVoucherMapper;
import com.lightdianping.service.ISeckillVoucherService;
import org.springframework.stereotype.Service;

/**
 * 秒杀优惠券表，与优惠券是一对一关系 服务实现类
 */
@Service
public class SeckillVoucherServiceImpl extends ServiceImpl<SeckillVoucherMapper, SeckillVoucher> implements ISeckillVoucherService {

}
