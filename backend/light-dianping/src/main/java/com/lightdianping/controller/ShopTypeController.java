package com.lightdianping.controller;

import com.lightdianping.dto.Result;
import com.lightdianping.service.IShopTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shop-type")
public class ShopTypeController {

    @Autowired
    private IShopTypeService typeService;

    @GetMapping("list")
    public Result queryTypeList() {
        return Result.ok(typeService.queryTypeList());
    }
}
