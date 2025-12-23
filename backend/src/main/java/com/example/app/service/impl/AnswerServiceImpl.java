// AnswerServiceImpl.java
package com.example.app.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.example.app.service.AnswerService;
import com.example.app.mapper.AnswerMapper;
import com.example.app.mapper.QuestionMapper;
import com.example.app.model.Answer;
import com.example.app.service.NotificationService;
import com.example.app.model.Notification;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private NotificationService notificationService;

    public Answer getById(Long id) {
        return answerMapper.selectById(id);
    }

    public List<Answer> listByQuestion(Long qid) {
        return answerMapper.selectByQuestionId(qid);
    }

    public List<Answer> listVisibleByQuestion(Long questionId, Long userId, boolean isAdmin) {
        return answerMapper.selectVisibleByQuestionId(questionId, userId, isAdmin);
    }

    public void create(Answer a) {
        // 设置默认状态
        if (a.getStatus() == null || a.getStatus().isEmpty()) {
            a.setStatus("PENDING");
        }
        // 设置默认最佳答案状态
        if (a.getIsBest() == null) {
            a.setIsBest(false);
        }
        // 设置创建时间
        if (a.getCreateTime() == null) {
            a.setCreateTime(LocalDateTime.now());
        }
        answerMapper.insert(a);
        
        // 发送通知：当有新回答时，通知问题创建者
        try {
            com.example.app.model.Question question = questionMapper.selectById(a.getQuestionId());
            if (question != null && question.getUserId() != null) {
                Notification notification = new Notification();
                notification.setType("NEW_ANSWER");
                notification.setUserId(question.getUserId()); // 通知问题创建者
                notification.setMessage("您的问题《" + question.getTitle() + "》收到了新的回答");
                notification.setTimestamp(LocalDateTime.now());
                
                // 尝试实时推送，如果用户离线则存储
                notificationService.sendNotification(notification);
                notificationService.storeOfflineNotification(question.getUserId().toString(), notification);
            }
        } catch (Exception e) {
            // 通知发送失败不影响回答创建
            e.printStackTrace();
        }
    }

    public void like(Long answerId, Long userId) {
        String key = "answer:like:set:" + answerId;
        Long added = redisTemplate.opsForSet().add(key, userId.toString());

        if (added != null && added == 1L) {
            redisTemplate.opsForValue().increment("answer:like:count:" + answerId);
            
            // 发送通知：当回答被点赞时，通知回答创建者
            try {
                Answer answer = answerMapper.selectById(answerId);
                if (answer != null && answer.getUserId() != null && !answer.getUserId().equals(userId)) {
                    // 不通知自己给自己点赞
                    Notification notification = new Notification();
                    notification.setType("ANSWER_LIKED");
                    notification.setUserId(answer.getUserId()); // 通知回答创建者
                    notification.setMessage("您的回答收到了新的点赞");
                    notification.setTimestamp(LocalDateTime.now());
                    
                    // 尝试实时推送，如果用户离线则存储
                    notificationService.sendNotification(notification);
                    notificationService.storeOfflineNotification(answer.getUserId().toString(), notification);
                }
            } catch (Exception e) {
                // 通知发送失败不影响点赞操作
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("Already liked");
        }
    }

    @Override
    @Transactional
    public void markAsBest(Long answerId, Long questionOwnerId) {
        // 验证权限：只有问题创建者可以标记最佳答案
        Answer answer = answerMapper.selectById(answerId);
        if (answer == null) {
            throw new RuntimeException("回答不存在");
        }

        Long questionId = answer.getQuestionId();
        com.example.app.model.Question question = questionMapper.selectById(questionId);
        if (question == null) {
            throw new RuntimeException("问题不存在");
        }

        // 检查权限
        if (!question.getUserId().equals(questionOwnerId)) {
            throw new RuntimeException("只有问题创建者可以标记最佳答案");
        }

        // 清除该问题现有的最佳答案标记
        answerMapper.clearBestAnswer(questionId);

        // 设置新的最佳答案
        answerMapper.updateIsBest(answerId, true);

        // 发送通知：当回答被标记为最佳答案时，通知回答创建者
        try {
            if (answer.getUserId() != null) {
        Notification notification = new Notification();
        notification.setType("BEST_ANSWER");
                notification.setUserId(answer.getUserId()); // 通知回答创建者
                notification.setMessage("您的回答被标记为最佳答案");
        notification.setTimestamp(LocalDateTime.now());
                
                // 尝试实时推送，如果用户离线则存储
                notificationService.sendNotification(notification);
                notificationService.storeOfflineNotification(answer.getUserId().toString(), notification);
            }
        } catch (Exception e) {
            // 通知发送失败不影响最佳答案标记操作
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void unmarkAsBest(Long answerId, Long questionOwnerId) {
        // 验证权限：只有问题创建者可以取消最佳答案标记
        Answer answer = answerMapper.selectById(answerId);
        if (answer == null) {
            throw new RuntimeException("回答不存在");
        }

        Long questionId = answer.getQuestionId();
        com.example.app.model.Question question = questionMapper.selectById(questionId);
        if (question == null) {
            throw new RuntimeException("问题不存在");
        }

        // 检查权限
        if (!question.getUserId().equals(questionOwnerId)) {
            throw new RuntimeException("只有问题创建者可以取消最佳答案标记");
        }

        // 取消最佳答案标记
        answerMapper.updateIsBest(answerId, false);
    }

    @Override
    public Answer getBestAnswer(Long questionId) {
        return answerMapper.selectBestAnswer(questionId);
    }

    @Override
    public boolean canMarkBestAnswer(Long userId, Long questionId) {
        com.example.app.model.Question question = questionMapper.selectById(questionId);
        return question != null && question.getUserId().equals(userId);
    }
}