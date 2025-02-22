package com.lightdianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lightdianping.dto.LoginFormDTO;
import com.lightdianping.dto.Result;
import com.lightdianping.entity.User;
import jakarta.servlet.http.HttpSession;

public interface IUserService extends IService<User> {

    Result sendCode(String phone, HttpSession session);

    Result login(LoginFormDTO loginForm, HttpSession session);
}
