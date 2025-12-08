// AnswerMapper.java
package com.example.app.mapper;

import com.example.app.model.Answer;
import java.util.List;

public interface AnswerMapper {
    Answer selectById(Long id);

    List<Answer> selectByQuestionId(Long qid);

    void insert(Answer a);
}