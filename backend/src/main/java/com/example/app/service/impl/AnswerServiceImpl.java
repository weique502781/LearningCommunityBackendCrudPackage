// AnswerServiceImpl.java
package com.example.app.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.example.app.service.AnswerService;
import com.example.app.mapper.AnswerMapper;
import com.example.app.model.Answer;
import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public Answer getById(Long id) {
        return answerMapper.selectById(id);
    }

    public List<Answer> listByQuestion(Long qid) {
        return answerMapper.selectByQuestionId(qid);
    }

    public void create(Answer a) {
        answerMapper.insert(a);
    }

    public void like(Long answerId, Long userId) {
        String key = "answer:like:set:" + answerId;
        Long added = redisTemplate.opsForSet().add(key, userId.toString());

        if (added != null && added == 1L) {
            redisTemplate.opsForValue().increment("answer:like:count:" + answerId);
        } else {
            throw new RuntimeException("Already liked");
        }
    }
}