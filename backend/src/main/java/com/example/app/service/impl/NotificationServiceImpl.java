package com.example.app.service.impl;

import com.example.app.model.Notification;
import com.example.app.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private RedisTemplate<String, Notification> redisTemplate;

    @Autowired
    private ChannelTopic topic;

    @Override
    public void sendNotification(Notification notification) {
        redisTemplate.convertAndSend(topic.getTopic(), notification);
    }

    @Override
    public void storeNotification(Notification notification) {
        redisTemplate.opsForList().rightPush("stored_notifications", notification);
    }

    @Override
    public List<Notification> getStoredNotifications() {
        return redisTemplate.opsForList().range("stored_notifications", 0, -1);
    }

    @Override
    public void storeOfflineNotification(String userId, Notification notification) {
        redisTemplate.opsForList().rightPush("offline:notifications:" + userId, notification);
    }

    @Override
    public List<Notification> getOfflineNotifications(String userId) {
        List<Notification> notifications = redisTemplate.opsForList().range("offline:notifications:" + userId, 0, -1);
        redisTemplate.delete("offline:notifications:" + userId); // Clear after retrieval
        return notifications;
    }

    @Override
    public void addNotification(Notification notification) {
        // Example implementation: store in a local list (or other storage mechanism)
        redisTemplate.opsForList().rightPush("notifications", notification);
    }

    @Override
    public List<Notification> getNotifications() {
        return redisTemplate.opsForList().range("notifications", 0, -1);
    }

    // 注意：RedisTemplate的序列化器配置已在RedisConfig中完成，这里不需要再配置
}