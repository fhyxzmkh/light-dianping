package com.lightdianping.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lightdianping.entity.ShopType;
import com.lightdianping.mapper.ShopTypeMapper;
import com.lightdianping.service.IShopTypeService;
import com.lightdianping.utils.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<ShopType> queryTypeList() {

        String typeListJson = stringRedisTemplate.opsForValue().get(RedisConstants.CACHE_SHOP_TYPE_KEY);

        if (StrUtil.isNotBlank(typeListJson)) {
            return JSONUtil.toList(typeListJson, ShopType.class);
        }

        List<ShopType> typeList = query().orderByAsc("sort").list();
        if (typeList == null) {
            return null;
        }

        stringRedisTemplate.opsForValue().set(RedisConstants.CACHE_SHOP_TYPE_KEY, JSONUtil.toJsonStr(typeList), RedisConstants.CACHE_SHOP_TYPE_TTL);

        return typeList;
    }

}
