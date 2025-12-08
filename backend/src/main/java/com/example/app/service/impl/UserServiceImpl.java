// UserServiceImpl.java
package com.example.app.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
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
        userMapper.insert(u);
    }
}