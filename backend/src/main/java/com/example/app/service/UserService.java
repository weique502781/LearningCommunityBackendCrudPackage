// UserService.java
package com.example.app.service;

import com.example.app.model.User;

public interface UserService {
    User getById(Long id);

    User getByEmail(String email);

    void create(User u);
}