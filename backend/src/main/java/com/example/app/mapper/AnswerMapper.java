package com.example.app.mapper;

import com.example.app.model.Answer;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/*
    AnswerMapper.java
    处理与回答相关的数据库操作
*/
public interface AnswerMapper {
    Answer selectById(Long id);//根据ID获取回答
    List<Answer> selectByQuestionId(Long qid);//根据问题ID获取回答列表
    List<Answer> selectVisibleByQuestionId(@Param("questionId") Long questionId, @Param("userId") Long userId, @Param("isAdmin") boolean isAdmin); // 带权限过滤的查询
    List<Answer> selectByUserId(Long userId);  // 按用户ID查询
    List<Answer> selectByStatus(String status);  // 按状态查询
    Answer selectBestAnswer(Long questionId);  // 获取问题的最佳答案
    void insert(Answer a);//插入新回答
    void updateIsBest(Long id, Boolean isBest);  // 更新最佳答案状态
    void updateStatus(Long id, String status);  // 更新状态
    void clearBestAnswer(Long questionId);  // 清除问题的最佳答案标记
    Long countAnswers();  // 统计回答数量
    Long countAnswersByStatus(String status);  // 按状态统计
}