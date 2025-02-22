package com.lightdianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lightdianping.dto.Result;
import com.lightdianping.entity.Shop;

public interface IShopService extends IService<Shop> {

    Result queryById(Long id);

    Result update(Shop shop);
}
