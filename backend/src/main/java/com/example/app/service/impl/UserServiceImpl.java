// UserServiceImpl.java
package com.example.app.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.example.app.service.UserService;
import com.example.app.mapper.UserMapper;
import com.example.app.model.User;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    public User getByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    public void create(User u) {
        // 设置默认角色
        if (u.getRole() == null || u.getRole().isEmpty()) {
            u.setRole("USER");
        }
        userMapper.insert(u);
    }

    @Override
    @Transactional
    public void updateUserRole(Long userId, String role) {
        userMapper.updateRole(userId, role);
    }

    @Override
    public boolean isAdmin(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null || user.getRole() == null) {
            return false;
        }
        String role = user.getRole().trim().toUpperCase();
        return "ADMIN".equals(role) || "SYSTEM_ADMIN".equals(role) || "SUPER_ADMIN".equals(role);
    }
}