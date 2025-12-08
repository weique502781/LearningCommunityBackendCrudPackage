package com.example.app.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.example.app.mapper")
public class MyBatisConfig {
    // 这个注解会告诉 MyBatis 扫描指定包下的所有 mapper 接口
}
