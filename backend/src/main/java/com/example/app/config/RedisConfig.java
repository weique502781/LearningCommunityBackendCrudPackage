package com.example.app.config;

import com.example.app.model.Notification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类：配置RedisTemplate Bean
 */
@Configuration
public class RedisConfig {

    /**
     * 配置RedisTemplate用于操作Notification对象
     * @param connectionFactory Redis连接工厂
     * @return RedisTemplate实例
     */
    @Bean
    public RedisTemplate<String, Notification> notificationRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Notification> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // 设置key序列化器
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        
        // 设置value序列化器
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        
        // 设置默认序列化器
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        
        template.afterPropertiesSet();
        return template;
    }
}

