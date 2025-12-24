// AdminServiceImpl.java
package com.example.app.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.example.app.service.AdminService;
import com.example.app.mapper.UserMapper;
import com.example.app.mapper.QuestionMapper;
import com.example.app.mapper.AnswerMapper;
import com.example.app.mapper.ResourceMapper;
import com.example.app.model.AdminStats;
import com.example.app.model.User;
import com.example.app.model.Question;
import com.example.app.model.Answer;
import com.example.app.model.Resource;
import com.example.app.model.Notification;
import com.example.app.service.NotificationService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private NotificationService notificationService;

    // 用户管理
    @Override
    public List<User> getAllUsers() {
        return userMapper.selectAll();
    }

    @Override
    public List<User> getUsersByRole(String role) {
        return userMapper.selectByRole(role);
    }

    @Override
    @Transactional
    public void updateUserRole(Long userId, String role) {
        userMapper.updateRole(userId, role);
    }

    @Override
    @Transactional
    public void banUser(Long userId) {
        // 这里可以根据需求扩展，比如添加封禁原因、封禁时间等
        // 目前简化为修改角色为BANNED
        userMapper.updateRole(userId, "BANNED");
    }

    @Override
    @Transactional
    public void unbanUser(Long userId) {
        userMapper.updateRole(userId, "USER");
    }

    // 内容审核
    @Override
    public List<Question> getPendingQuestions() {
        return questionMapper.selectByStatus("PENDING");
    }

    @Override
    public List<Answer> getPendingAnswers() {
        return answerMapper.selectByStatus("PENDING");
    }

    @Override
    public List<Resource> getPendingResources() {
        return resourceMapper.selectByStatus("PENDING");
    }

    @Override
    @Transactional
    public void approveQuestion(Long questionId) {
        questionMapper.updateStatus(questionId, "APPROVED");
        
        // 发送通知给问题创建者
        try {
            Question question = questionMapper.selectById(questionId);
            if (question != null && question.getUserId() != null) {
                Notification notification = new Notification();
                notification.setType("QUESTION_APPROVED");
                notification.setUserId(question.getUserId());
                notification.setQuestionId(questionId);
                notification.setMessage("您的问题《" + question.getTitle() + "》已通过审核");
                notification.setTimestamp(LocalDateTime.now());
                
                notificationService.sendNotification(notification);
                notificationService.storeOfflineNotification(question.getUserId().toString(), notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void rejectQuestion(Long questionId) {
        questionMapper.updateStatus(questionId, "REJECTED");
        
        // 发送通知给问题创建者
        try {
            Question question = questionMapper.selectById(questionId);
            if (question != null && question.getUserId() != null) {
                Notification notification = new Notification();
                notification.setType("QUESTION_REJECTED");
                notification.setUserId(question.getUserId());
                notification.setQuestionId(questionId);
                notification.setMessage("您的问题《" + question.getTitle() + "》未通过审核");
                notification.setTimestamp(LocalDateTime.now());
                
                notificationService.sendNotification(notification);
                notificationService.storeOfflineNotification(question.getUserId().toString(), notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void approveAnswer(Long answerId) {
        answerMapper.updateStatus(answerId, "APPROVED");
        
        // 发送通知给回答创建者和问题创建者
        try {
            Answer answer = answerMapper.selectById(answerId);
            if (answer != null) {
                // 通知回答创建者
                if (answer.getUserId() != null) {
                    Notification notification = new Notification();
                    notification.setType("ANSWER_APPROVED");
                    notification.setUserId(answer.getUserId());
                    notification.setAnswerId(answerId);
                    notification.setQuestionId(answer.getQuestionId());
                    notification.setMessage("您的回答已通过审核");
                    notification.setTimestamp(LocalDateTime.now());
                    
                    notificationService.sendNotification(notification);
                    notificationService.storeOfflineNotification(answer.getUserId().toString(), notification);
                }
                
                // 通知问题创建者有新回答
                Question question = questionMapper.selectById(answer.getQuestionId());
                if (question != null && question.getUserId() != null && !question.getUserId().equals(answer.getUserId())) {
                    Notification notification = new Notification();
                    notification.setType("NEW_ANSWER_APPROVED");
                    notification.setUserId(question.getUserId());
                    notification.setAnswerId(answerId);
                    notification.setQuestionId(answer.getQuestionId());
                    notification.setMessage("您的问题《" + question.getTitle() + "》收到了新的回答");
                    notification.setTimestamp(LocalDateTime.now());
                    
                    notificationService.sendNotification(notification);
                    notificationService.storeOfflineNotification(question.getUserId().toString(), notification);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void rejectAnswer(Long answerId) {
        answerMapper.updateStatus(answerId, "REJECTED");
        
        // 发送通知给回答创建者
        try {
            Answer answer = answerMapper.selectById(answerId);
            if (answer != null && answer.getUserId() != null) {
                Notification notification = new Notification();
                notification.setType("ANSWER_REJECTED");
                notification.setUserId(answer.getUserId());
                notification.setAnswerId(answerId);
                notification.setQuestionId(answer.getQuestionId());
                notification.setMessage("您的回答未通过审核");
                notification.setTimestamp(LocalDateTime.now());
                
                notificationService.sendNotification(notification);
                notificationService.storeOfflineNotification(answer.getUserId().toString(), notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void approveResource(Long resourceId) {
        resourceMapper.updateStatus(resourceId, "APPROVED");
        
        // 发送通知给资源创建者
        try {
            Resource resource = resourceMapper.selectById(resourceId);
            if (resource != null && resource.getUserId() != null) {
                Notification notification = new Notification();
                notification.setType("RESOURCE_APPROVED");
                notification.setUserId(resource.getUserId());
                notification.setResourceId(resourceId);
                notification.setMessage("您的资源《" + resource.getTitle() + "》已通过审核");
                notification.setTimestamp(LocalDateTime.now());
                
                notificationService.sendNotification(notification);
                notificationService.storeOfflineNotification(resource.getUserId().toString(), notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void rejectResource(Long resourceId) {
        resourceMapper.updateStatus(resourceId, "REJECTED");
        
        // 发送通知给资源创建者
        try {
            Resource resource = resourceMapper.selectById(resourceId);
            if (resource != null && resource.getUserId() != null) {
                Notification notification = new Notification();
                notification.setType("RESOURCE_REJECTED");
                notification.setUserId(resource.getUserId());
                notification.setResourceId(resourceId);
                notification.setMessage("您的资源《" + resource.getTitle() + "》未通过审核");
                notification.setTimestamp(LocalDateTime.now());
                
                notificationService.sendNotification(notification);
                notificationService.storeOfflineNotification(resource.getUserId().toString(), notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void deleteQuestion(Long questionId) {
        // 这里应该先删除相关的回答，再删除问题
        // 简化处理，实际项目中需要考虑外键约束
        // questionMapper.deleteById(questionId);
        // 暂时只更新状态为已删除
        questionMapper.updateStatus(questionId, "DELETED");
    }

    @Override
    @Transactional
    public void deleteAnswer(Long answerId) {
        answerMapper.updateStatus(answerId, "DELETED");
    }

    @Override
    @Transactional
    public void deleteResource(Long resourceId) {
        resourceMapper.updateStatus(resourceId, "DELETED");
    }

    // 统计分析
    @Override
    public AdminStats getSystemStats() {
        AdminStats stats = new AdminStats();

        stats.setTotalUsers(userMapper.countUsers().longValue());
        stats.setTotalQuestions(questionMapper.countQuestions().longValue());
        stats.setTotalAnswers(answerMapper.countAnswers().longValue());
        stats.setTotalResources(resourceMapper.countResources().longValue());

        // 待审核数量
        long pendingQuestions = questionMapper.countQuestionsByStatus("PENDING");
        long pendingAnswers = answerMapper.countAnswersByStatus("PENDING");
        long pendingResources = resourceMapper.countResourcesByStatus("PENDING");
        stats.setPendingReviews(pendingQuestions + pendingAnswers + pendingResources);

        return stats;
    }

    @Override
    public Map<String, Long> getDailyStats(int days) {
        // 这里简化实现，实际需要按日期分组统计
        // 可以使用SQL的GROUP BY DATE(create_time)实现
        Map<String, Long> stats = new HashMap<>();

        // 示例数据，实际需要从数据库查询
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 0; i < days; i++) {
            LocalDate date = today.minusDays(i);
            String dateStr = date.format(formatter);
            // 这里应该查询数据库获取当天的数据
            stats.put(dateStr, (long) (Math.random() * 100)); // 示例数据
        }

        return stats;
    }
}
