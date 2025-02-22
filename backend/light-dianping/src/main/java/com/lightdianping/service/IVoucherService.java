package com.lightdianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lightdianping.dto.Result;
import com.lightdianping.entity.Voucher;

public interface IVoucherService extends IService<Voucher> {

    Result queryVoucherOfShop(Long shopId);

    void addSeckillVoucher(Voucher voucher);
}
