package com.example.app.config;

import com.example.app.model.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类：配置RedisTemplate Bean
 */
@Configuration
public class RedisConfig {

    @Bean
    public Jackson2JsonRedisSerializer<Notification> notificationJsonRedisSerializer(ObjectMapper objectMapper) {
        Jackson2JsonRedisSerializer<Notification> serializer = new Jackson2JsonRedisSerializer<>(Notification.class);
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }

    /**
     * 配置RedisTemplate用于操作Notification对象
     * connectionFactory Redis连接工厂
     * RedisTemplate实例
     */
    @Bean
    public RedisTemplate<String, Notification> notificationRedisTemplate(
            RedisConnectionFactory connectionFactory,
            Jackson2JsonRedisSerializer<Notification> notificationJsonRedisSerializer) {
        RedisTemplate<String, Notification> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // 设置key序列化器
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        
        // 设置value序列化器
        template.setValueSerializer(notificationJsonRedisSerializer);
        template.setHashValueSerializer(notificationJsonRedisSerializer);
        
        // 设置默认序列化器
        template.setDefaultSerializer(notificationJsonRedisSerializer);
        
        template.afterPropertiesSet();
        return template;
    }
}

