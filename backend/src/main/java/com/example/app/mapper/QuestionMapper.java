// QuestionMapper.java
package com.example.app.mapper;

import com.example.app.model.Question;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface QuestionMapper {
    Question selectById(Long id);//根据ID获取问题
    List<Question> selectAll();//获取所有问题
    List<Question> selectVisible(@Param("userId") Long userId, @Param("isAdmin") boolean isAdmin); // 带权限过滤的查询
    List<Question> selectByUserId(Long userId);  // 按用户ID查询
    List<Question> selectByStatus(String status);  // 按状态查询
    void insert(Question q);//插入新问题
    void updateStatus(Long id, String status);  // 更新状态
    Long countQuestions();  // 统计问题数量
    Long countQuestionsByStatus(String status);  // 按状态统计
}