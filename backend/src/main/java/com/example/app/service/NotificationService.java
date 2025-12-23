package com.example.app.service;

import com.example.app.model.Notification;
import java.util.List;

public interface NotificationService {

    List<Notification> getNotifications();

    void addNotification(Notification notification);

    void storeOfflineNotification(String userId, Notification notification);

    List<Notification> getOfflineNotifications(String userId);

    void sendNotification(Notification notification);

    void storeNotification(Notification notification);

    List<Notification> getStoredNotifications();
}