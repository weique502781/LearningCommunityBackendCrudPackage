package com.example.app.config;

import com.example.app.model.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * WebSocket处理器：管理用户会话并监听Redis消息进行实时推送
 */
@Component
public class NotificationWebSocketHandler extends TextWebSocketHandler implements MessageListener {

    // 存储用户ID到WebSocket会话的映射
    private final ConcurrentMap<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 处理WebSocket连接建立
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 从请求参数中获取userId（例如：ws://localhost:8081/ws/notifications?userId=123）
        String userId = getUserIdFromSession(session);
        if (userId != null && !userId.isEmpty()) {
            userSessions.put(userId, session);
            System.out.println("用户 " + userId + " 已连接WebSocket");
            
            // 发送连接成功消息
            session.sendMessage(new TextMessage("{\"type\":\"CONNECTED\",\"message\":\"WebSocket连接成功\"}"));
        } else {
            session.close(CloseStatus.BAD_DATA.withReason("缺少userId参数"));
        }
    }

    /**
     * 处理WebSocket连接关闭
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserIdFromSession(session);
        if (userId != null) {
            userSessions.remove(userId);
            System.out.println("用户 " + userId + " 已断开WebSocket连接");
        }
    }

    /**
     * 处理客户端发送的文本消息
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 可以处理客户端的心跳消息或其他控制消息
        String payload = message.getPayload();
        if ("PING".equals(payload)) {
            session.sendMessage(new TextMessage("PONG"));
        }
    }

    /**
     * 从WebSocket会话中提取userId
     */
    private String getUserIdFromSession(WebSocketSession session) {
        // 优先从查询参数获取
        String query = session.getUri().getQuery();
        if (query != null) {
            // 支持多参数格式，如 userId=123&token=xxx
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("userId=")) {
                    return param.substring("userId=".length());
                }
            }
        }
        // 也可以从session属性中获取（如果前端在握手时设置了）
        Object attrUserId = session.getAttributes().get("userId");
        if (attrUserId != null) {
            return attrUserId.toString();
        }
        return null;
    }

    /**
     * Redis消息监听器：当收到Redis Pub/Sub消息时，推送给对应的用户
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // 反序列化Redis消息为Notification对象
            GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
            Notification notification = (Notification) serializer.deserialize(message.getBody());
            
            if (notification != null && notification.getUserId() != null) {
                String userId = notification.getUserId().toString();
                WebSocketSession session = userSessions.get(userId);
                
                if (session != null && session.isOpen()) {
                    // 用户在线，实时推送
                    String jsonMessage = objectMapper.writeValueAsString(notification);
                    session.sendMessage(new TextMessage(jsonMessage));
                    System.out.println("实时推送通知给用户 " + userId + ": " + notification.getMessage());
                } else {
                    // 用户离线，消息已通过storeOfflineNotification存储
                    System.out.println("用户 " + userId + " 离线，通知已存储");
                }
            }
        } catch (IOException e) {
            System.err.println("推送WebSocket消息失败: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("处理Redis消息失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取当前在线的用户数量
     */
    public int getOnlineUserCount() {
        return userSessions.size();
    }
}