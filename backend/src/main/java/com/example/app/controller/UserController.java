package com.example.app.controller;

import com.example.app.model.User;
import com.example.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/*
    UserController.java
    处理用户相关的HTTP请求
*/
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;//处理用户相关操作

    // 根据ID获取用户信息
    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return userService.getById(id);
    }

    // 根据邮箱获取用户信息
    @GetMapping("/email/{email}")
    public User getByEmail(@PathVariable String email) {
        return userService.getByEmail(email);
    }
}