package com.lightdianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lightdianping.entity.ShopType;

import java.util.List;

public interface IShopTypeService extends IService<ShopType> {

    List<ShopType> queryTypeList();
}
