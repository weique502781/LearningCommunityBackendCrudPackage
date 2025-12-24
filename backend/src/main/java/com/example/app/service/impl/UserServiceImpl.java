// UserServiceImpl.java-用于实现用户服务的具体逻辑
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
    private UserMapper userMapper;// 数据库操作映射器

    //根据ID获取用户信息
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    //根据邮箱获取用户信息
    public User getByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    //创建新用户，设置默认角色
    public void create(User u) {
        // 设置默认角色
        if (u.getRole() == null || u.getRole().isEmpty()) {
            u.setRole("USER");
        }
        userMapper.insert(u);
    }

    //更新用户角色
    @Override
    @Transactional
    public void updateUserRole(Long userId, String role) {
        userMapper.updateRole(userId, role);
    }

    //检查用户是否为管理员
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