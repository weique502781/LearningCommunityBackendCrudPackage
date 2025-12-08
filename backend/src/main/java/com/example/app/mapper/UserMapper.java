// UserMapper.java
package com.example.app.mapper;

import com.example.app.model.User;

public interface UserMapper {
    User selectById(Long id);

    User selectByEmail(String email);

    void insert(User user);
}