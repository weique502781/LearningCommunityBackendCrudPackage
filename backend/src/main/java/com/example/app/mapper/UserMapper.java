package com.example.app.mapper;

import com.example.app.model.User;

import java.util.List;

/*
    UserMapper.java
    处理与用户相关的数据库操作
*/
public interface UserMapper {
    User selectById(Long id);//根据ID获取用户
    User selectByEmail(String email);//根据邮箱获取用户
    List<User> selectAll();//获取所有用户
    List<User> selectByRole(String role);//根据角色获取用户列表
    void insert(User user);//插入新用户
    void update(User user);//更新用户信息
    void updateRole(Long userId,String role);//更新用户角色
    Long countUsers();//统计用户数量
}