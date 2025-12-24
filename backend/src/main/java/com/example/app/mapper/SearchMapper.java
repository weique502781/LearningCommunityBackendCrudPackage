package com.example.app.mapper;

import com.example.app.model.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/*
    SearchMapper.java
    处理搜索相关的数据库操作
*/
@Mapper
public interface SearchMapper {

    /**
     * 搜索问题：返回完整的Question对象
     * @param keyword 搜索关键词
     * @param offset 偏移量
     * @param size 每页大小
     * @param orderBy 排序字段（create_time, title等）
     * @param orderDirection 排序方向（ASC, DESC）
     * @param userId 当前用户ID
     * @param isAdmin 是否管理员
     * @return Question列表
     */
    @Select({
        "<script>",
        "SELECT id, user_id, title, content, status, create_time",
        "FROM questions",
        "WHERE status != 'DELETED' AND MATCH(title, content) AGAINST(#{keyword} IN BOOLEAN MODE)",
        "<if test='!isAdmin'>",
        "AND (status = 'APPROVED'",
        "<if test='userId != null'>",
        "OR user_id = #{userId}",
        "</if>",
        ")",
        "</if>",
        "<if test='orderBy != null and orderBy != \"\"'>",
        "ORDER BY ${orderBy}",
        "<if test='orderDirection != null and orderDirection != \"\"'>",
        "${orderDirection}",
        "</if>",
        "</if>",
        "LIMIT #{size} OFFSET #{offset}",
        "</script>"
    })
    List<Question> search(
        @Param("keyword") String keyword,
        @Param("offset") int offset,
        @Param("size") int size,
        @Param("orderBy") String orderBy,
        @Param("orderDirection") String orderDirection,
        @Param("userId") Long userId,
        @Param("isAdmin") boolean isAdmin
    );
}