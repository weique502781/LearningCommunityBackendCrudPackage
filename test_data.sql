-- test_data_final.sql
-- 1. 创建数据库（若不存在）
CREATE DATABASE IF NOT EXISTS learning_db DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
USE learning_db;

-- 2. 删除现有表（如果存在）以重新创建
DROP TABLE IF EXISTS resources;
DROP TABLE IF EXISTS answers;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS users;

-- 3. 创建核心表（含所有新字段）
-- 用户表
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(100),
    role VARCHAR(20) DEFAULT 'USER' COMMENT '用户角色：USER, ADMIN, BANNED',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 问题表
CREATE TABLE questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态：PENDING, APPROVED, REJECTED, DELETED',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_questions_user_id (user_id),
    INDEX idx_questions_status (status),
    INDEX idx_questions_title (title(100))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问题表';

-- 回答表
CREATE TABLE answers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    is_best BOOLEAN DEFAULT FALSE COMMENT '是否是最佳答案',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态：PENDING, APPROVED, REJECTED, DELETED',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_answers_question_id (question_id),
    INDEX idx_answers_user_id (user_id),
    INDEX idx_answers_is_best (is_best),
    INDEX idx_answers_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='回答表';

-- 资源表
CREATE TABLE resources (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    url VARCHAR(500) UNIQUE,
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态：PENDING, APPROVED, REJECTED, DELETED',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_resources_user_id (user_id),
    INDEX idx_resources_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源表';

-- 4. 插入测试数据
-- 插入用户数据（密码为明文，实际生产环境应使用加密密码）
INSERT INTO users (email, password, nickname, role) VALUES
('admin@example.com', 'password123', '系统管理员', 'ADMIN'),
('moderator@example.com', 'password123', '内容审核员', 'ADMIN'),
('alice@example.com', 'password123', 'Alice', 'USER'),
('bob@example.com', 'password123', 'Bob', 'USER'),
('charlie@example.com', 'password123', 'Charlie', 'USER'),
('david@example.com', 'password123', 'David', 'USER'),
('eva@example.com', 'password123', 'Eva', 'USER'),
('banned@example.com', 'password123', '被封禁用户', 'BANNED');

-- 插入问题数据
INSERT INTO questions (user_id, title, content, status, create_time) VALUES
(3, '如何学习Spring Boot？', '想系统学习Spring Boot，有什么好的建议？', 'APPROVED', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(4, '数据库优化有哪些技巧？', 'MySQL数据库性能优化的常用方法有哪些？', 'APPROVED', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(5, 'Vue和React哪个更适合新手？', '作为前端新手，应该选择Vue还是React？', 'PENDING', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(6, 'Redis在Spring Boot中如何集成？', '想在Spring Boot项目中使用Redis缓存，如何配置？', 'APPROVED', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(7, 'MyBatis和JPA哪个更好？', '在Spring Boot项目中，应该选择MyBatis还是JPA？', 'REJECTED', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(3, '如何设计RESTful API？', '设计良好的RESTful API应该遵循哪些原则？', 'APPROVED', NOW());

-- 插入回答数据
INSERT INTO answers (question_id, user_id, content, is_best, status, create_time) VALUES
(1, 4, '可以从官方文档开始学习，然后找一些实战项目练习。推荐《Spring Boot实战》这本书。', true, 'APPROVED', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(1, 5, 'B站有很多Spring Boot的免费教程，可以跟着视频一步步学习。', false, 'APPROVED', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(1, 6, '建议先学习Spring Core，再学习Spring Boot，这样理解更深入。', false, 'PENDING', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(2, 3, '1. 合理使用索引 2. 优化SQL查询 3. 分库分表 4. 使用缓存', true, 'APPROVED', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(2, 7, 'EXPLAIN分析SQL执行计划很重要，可以帮助发现性能瓶颈。', false, 'APPROVED', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(3, 4, 'Vue的学习曲线更平缓，文档友好，适合新手入门。', false, 'PENDING', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(4, 5, '添加spring-boot-starter-data-redis依赖，配置application.yml即可。', true, 'APPROVED', NOW()),
(6, 4, 'RESTful API设计原则：1. 使用名词复数 2. 合理使用HTTP方法 3. 版本控制 4. 错误处理规范', false, 'APPROVED', NOW());

-- 插入资源数据
INSERT INTO resources (user_id, title, url, status, create_time) VALUES
(3, 'Spring Boot官方文档', 'https://spring.io/projects/spring-boot', 'APPROVED', DATE_SUB(NOW(), INTERVAL 7 DAY)),
(4, 'MySQL官方文档', 'https://dev.mysql.com/doc/', 'APPROVED', DATE_SUB(NOW(), INTERVAL 6 DAY)),
(5, 'Vue.js官方教程', 'https://vuejs.org/guide/introduction.html', 'PENDING', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(6, 'Redis官方文档', 'https://redis.io/documentation', 'APPROVED', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(7, 'RESTful API设计指南', 'https://restfulapi.net/', 'REJECTED', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(3, 'MyBatis官方文档', 'https://mybatis.org/mybatis-3/zh/index.html', 'APPROVED', NOW());

-- 5. 输出数据统计
SELECT 'Users:' as Table_Name, COUNT(*) as Count FROM users
UNION ALL
SELECT 'Questions:', COUNT(*) FROM questions
UNION ALL
SELECT 'Answers:', COUNT(*) FROM answers
UNION ALL
SELECT 'Resources:', COUNT(*) FROM resources;