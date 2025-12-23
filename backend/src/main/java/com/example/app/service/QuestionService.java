// QuestionService.java
package com.example.app.service;

import com.example.app.model.Question;
import java.util.List;

public interface QuestionService {
    Question getById(Long id);

    List<Question> listAll();

    List<Question> listVisible(Long userId, boolean isAdmin); // 获取可见的问题列表

    Question getVisibleById(Long id, Long userId, boolean isAdmin); // 获取可见的单个问题

    void create(Question q);
}