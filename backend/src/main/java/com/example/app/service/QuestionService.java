// QuestionService.java
package com.example.app.service;

import com.example.app.model.Question;
import java.util.List;

public interface QuestionService {
    Question getById(Long id);

    List<Question> listAll();

    void create(Question q);
}