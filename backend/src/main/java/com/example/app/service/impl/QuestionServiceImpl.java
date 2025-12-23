// QuestionServiceImpl.java
package com.example.app.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.app.service.QuestionService;
import com.example.app.mapper.QuestionMapper;
import com.example.app.model.Question;
import java.time.LocalDateTime;
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

    public List<Question> listVisible(Long userId, boolean isAdmin) {
        return questionMapper.selectVisible(userId, isAdmin);
    }

    public Question getVisibleById(Long id, Long userId, boolean isAdmin) {
        Question q = questionMapper.selectById(id);
        if (q == null) return null;
        
        // 管理员可见所有
        if (isAdmin) return q;
        
        // APPROVED状态可见
        if ("APPROVED".equals(q.getStatus())) return q;
        
        // 作者可见
        if (userId != null && userId.equals(q.getUserId())) return q;
        
        // 其他情况不可见
        return null;
    }

    public void create(Question q) {
        if (q.getStatus() == null || q.getStatus().isEmpty()) {
            q.setStatus("PENDING");
        }
        if (q.getCreateTime() == null) {
            q.setCreateTime(LocalDateTime.now());
        }
        questionMapper.insert(q);
    }
}