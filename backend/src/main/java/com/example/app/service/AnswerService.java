// AnswerService.java
package com.example.app.service;

import com.example.app.model.Answer;
import java.util.List;

public interface AnswerService {
    Answer getById(Long id);// 根据ID获取答案
    List<Answer> listByQuestion(Long qid);// 根据问题ID获取答案列表
    List<Answer> listVisibleByQuestion(Long questionId, Long userId, boolean isAdmin); // 获取可见的回答列表
    void create(Answer a);// 创建新答案
    void like(Long answerId, Long userId);// 点赞答案
    void markAsBest(Long answerId, Long questionOwnerId) throws RuntimeException;  // 标记最佳答案
    void unmarkAsBest(Long answerId, Long questionOwnerId) throws RuntimeException;  // 取消最佳答案标记
    Answer getBestAnswer(Long questionId);  // 获取最佳答案
    boolean canMarkBestAnswer(Long userId, Long questionId);  // 检查是否可以标记最佳答案
}