// AnswerService.java
package com.example.app.service;

import com.example.app.model.Answer;
import java.util.List;

public interface AnswerService {
    Answer getById(Long id);

    List<Answer> listByQuestion(Long qid);

    void create(Answer a);

    void like(Long answerId, Long userId);
}