// AdminService.java
package com.example.app.service;

import com.example.app.model.AdminStats;
import com.example.app.model.User;
import com.example.app.model.Question;
import com.example.app.model.Answer;
import com.example.app.model.Resource;
import java.util.List;
import java.util.Map;

public interface AdminService {
    // 用户管理
    List<User> getAllUsers();
    List<User> getUsersByRole(String role);
    void updateUserRole(Long userId, String role);
    void banUser(Long userId);
    void unbanUser(Long userId);

    // 内容审核
    List<Question> getPendingQuestions();
    List<Answer> getPendingAnswers();
    List<Resource> getPendingResources();

    void approveQuestion(Long questionId);
    void rejectQuestion(Long questionId);
    void approveAnswer(Long answerId);
    void rejectAnswer(Long answerId);
    void approveResource(Long resourceId);
    void rejectResource(Long resourceId);

    void deleteQuestion(Long questionId);
    void deleteAnswer(Long answerId);
    void deleteResource(Long resourceId);

    // 统计分析
    AdminStats getSystemStats();
    Map<String, Long> getDailyStats(int days);
}
