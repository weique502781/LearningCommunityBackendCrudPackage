// QuestionMapper.java
package com.example.app.mapper;

import com.example.app.model.Question;
import java.util.List;

public interface QuestionMapper {
    Question selectById(Long id);

    List<Question> selectAll();

    void insert(Question q);
}