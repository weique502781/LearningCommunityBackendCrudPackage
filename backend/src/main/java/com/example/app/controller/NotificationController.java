package com.example.app.controller;

import com.example.app.model.Notification;
import com.example.app.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/*
    NotificationController.java
    处理通知相关的HTTP请求
*/
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final List<Notification> notifications = new ArrayList<>();

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public List<Notification> getNotifications() {
        return notifications;
    }

    @PostMapping
    public void sendNotification(@RequestBody Notification notification) {
        notifications.add(notification);
    }

    @GetMapping("/offline/{userId}")
    public List<Notification> getOfflineNotifications(@PathVariable String userId) {
        return notificationService.getOfflineNotifications(userId);
    }
}