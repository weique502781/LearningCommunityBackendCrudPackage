// User.java
package com.example.app.model;

import java.time.LocalDateTime;

public class User {
    private Long id;// 用户ID
    private String email;// 邮箱
    private String password;//密码
    private String nickname;//昵称
    private String role;  // 角色：USER, ADMIN
    private LocalDateTime createTime;//创建时间

    // 构造函数
    public User() {}

    public User(String email, String password, String nickname, String role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    // getter和setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    // 工具方法
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
}