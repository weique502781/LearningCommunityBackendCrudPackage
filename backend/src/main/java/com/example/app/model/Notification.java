// Notification.java--模型类，用于表示通知实体
package com.example.app.model;

import java.time.LocalDateTime;

public class Notification {
    private Long id;// 通知ID
    private Long userId;  // 关联的用户ID，用于指定通知接收者
    private Long questionId;// 关联的问题ID（如果适用）
    private Long answerId;// 关联的答案ID（如果适用）
    private Long resourceId;// 关联的资源ID（如果适用）
    private String type;// 通知类型，如“NEW_ANSWER”、“RESOURCE_APPROVED”等
    private String message;// 通知消息内容
    private LocalDateTime timestamp;// 通知创建时间

    // Getters and setters
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

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}