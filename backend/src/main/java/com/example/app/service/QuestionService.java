// QuestionService.java-用于定义与问题相关的业务逻辑接口
package com.example.app.service;

import com.example.app.model.Question;
import java.util.List;

public interface QuestionService {
    Question getById(Long id);// 根据ID获取问题

    List<Question> listAll();// 获取所有问题

    List<Question> listVisible(Long userId, boolean isAdmin); // 获取可见的问题列表

    Question getVisibleById(Long id, Long userId, boolean isAdmin); // 获取可见的单个问题

    void create(Question q);// 创建新问题
}