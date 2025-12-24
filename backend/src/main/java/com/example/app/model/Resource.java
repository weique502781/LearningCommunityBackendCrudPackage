// Resource.java--模型类，表示资源实体
package com.example.app.model;

import java.time.LocalDateTime;

public class Resource {
    private Long id;// 资源ID
    private Long userId;// 提交资源的用户ID
    private String title;// 资源标题
    private String url;// 资源链接
    private String status; // 资源状态：待定、批准、拒绝
    private LocalDateTime createTime; // 创建时间字段

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}