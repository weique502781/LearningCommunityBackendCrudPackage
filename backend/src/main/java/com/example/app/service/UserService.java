// UserService.java
package com.example.app.service;

import com.example.app.model.User;

public interface UserService {
    User getById(Long id);// 根据ID获取用户
    User getByEmail(String email);// 根据邮箱获取用户
    void create(User u);// 创建新用户
    void updateUserRole(Long userId, String role);  // 更新用户角色
    boolean isAdmin(Long userId);  // 检查是否是管理员
}