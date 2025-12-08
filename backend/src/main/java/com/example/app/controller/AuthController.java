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
        userService.create(user);
        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        return result;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        User u = userService.getByEmail(email);
        if (u == null) {
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