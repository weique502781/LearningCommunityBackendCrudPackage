//NotificationService.java-用于处理通知相关的服务接口
package com.example.app.service;

import com.example.app.model.Notification;
import java.util.List;

public interface NotificationService {

    List<Notification> getNotifications();//获取所有通知

    void addNotification(Notification notification);//添加新通知

    void storeOfflineNotification(String userId, Notification notification);//存储离线通知

    List<Notification> getOfflineNotifications(String userId);//获取离线通知

    void sendNotification(Notification notification);//发送通知

    void storeNotification(Notification notification);//存储通知

    List<Notification> getStoredNotifications();//获取存储的通知
}