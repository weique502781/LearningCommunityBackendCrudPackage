// AuthController.java
package com.example.app.controller;

import com.example.app.security.JwtUtil;
import com.example.app.model.User;
import com.example.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        // 检查是否管理员注册（需要有特殊标识）
        // 这里简化处理，实际应该更安全
        if (user.getRole() != null && user.getRole().equals("ADMIN")) {
            // 管理员注册需要特殊权限或验证码
            // 这里可以添加额外的验证逻辑
            Map<String, Object> result = new HashMap<>();
            result.put("ok", false);
            result.put("error", "管理员注册需要特殊权限");
            return result;
        }

        userService.create(user);
        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        result.put("message", "注册成功");
        return result;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        User u = userService.getByEmail(email);
        
        if (u == null || !u.getPassword().equals(password)) {
            Map<String, Object> result = new HashMap<>();
            result.put("error", "invalid credentials");
            return result;
        }

        String token = jwtUtil.generateToken(u.getId(), u.getNickname());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        return result;
    }
}