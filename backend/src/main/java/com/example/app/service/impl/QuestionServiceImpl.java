// QuestionServiceImpl.java
package com.example.app.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.app.service.QuestionService;
import com.example.app.mapper.QuestionMapper;
import com.example.app.model.Question;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    public Question getById(Long id) {
        return questionMapper.selectById(id);
    }

    public List<Question> listAll() {
        return questionMapper.selectAll();
    }

    public void create(Question q) {
        questionMapper.insert(q);
    }
}