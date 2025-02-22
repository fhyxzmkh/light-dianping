package com.lightdianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lightdianping.dto.Result;
import com.lightdianping.entity.VoucherOrder;

public interface IVoucherOrderService extends IService<VoucherOrder> {

    Result seckillVoucher(Long voucherId);

    Result createVoucherOrder(Long voucherId);
}
