//NotificationServiceImpl.java-用于实现通知服务的具体逻辑，包括发送和存储通知。
package com.example.app.service.impl;

import com.example.app.model.Notification;
import com.example.app.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    @Qualifier("notificationRedisTemplate")
    private RedisTemplate<String, Notification> redisTemplate;//Redis交互的RedisTemplate

    @Autowired
    private ChannelTopic topic;//Redis频道主题

    // 发送通知：将通知发布到Redis频道
    @Override
    public void sendNotification(Notification notification) {
        redisTemplate.convertAndSend(topic.getTopic(), notification);
    }

    // 存储通知：将通知存储在Redis列表中
    @Override
    public void storeNotification(Notification notification) {
        redisTemplate.opsForList().rightPush("stored_notifications", notification);
    }

    // 获取存储的通知列表
    @Override
    public List<Notification> getStoredNotifications() {
        return redisTemplate.opsForList().range("stored_notifications", 0, -1);
    }

    // 存储离线通知
    @Override
    public void storeOfflineNotification(String userId, Notification notification) {
        redisTemplate.opsForList().rightPush("offline:notifications:" + userId, notification);
    }

    // 获取并清除离线通知
    @Override
    public List<Notification> getOfflineNotifications(String userId) {
        List<Notification> notifications = redisTemplate.opsForList().range("offline:notifications:" + userId, 0, -1);
        redisTemplate.delete("offline:notifications:" + userId); // Clear after retrieval
        return notifications;
    }

    // 添加通知到系统（示例实现）
    @Override
    public void addNotification(Notification notification) {
        // Example implementation: store in a local list (or other storage mechanism)
        redisTemplate.opsForList().rightPush("notifications", notification);
    }

    // 获取所有系统通知（示例实现）
    @Override
    public List<Notification> getNotifications() {
        return redisTemplate.opsForList().range("notifications", 0, -1);
    }

    // 注意：RedisTemplate的序列化器配置已在RedisConfig中完成，这里不需要再配置
}