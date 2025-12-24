//SearchService.java-用于处理搜索相关的业务逻辑
package com.example.app.service;

import com.example.app.mapper.SearchMapper;
import com.example.app.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    private SearchMapper searchMapper;//数据访问对象

    /**
     * 搜索问题
     * @param keyword 搜索关键词
     * @param page 页码（从1开始）
     * @param size 每页大小
     * @param orderBy 排序字段（可选：create_time, title等，默认为create_time）
     * @param orderDirection 排序方向（可选：ASC, DESC，默认为DESC）
     * @param userId 当前用户ID（可选）
     * @param isAdmin 是否管理员
     * @return Question列表
     */
    public List<Question> search(String keyword, int page, int size, String orderBy, String orderDirection, Long userId, boolean isAdmin) {
        int offset = (page - 1) * size;
        
        // 默认按创建时间降序排序
        if (orderBy == null || orderBy.isEmpty()) {
            orderBy = "create_time";
        }
        if (orderDirection == null || orderDirection.isEmpty()) {
            orderDirection = "DESC";
        }
        
        // 验证排序字段，防止SQL注入
        if (!isValidOrderBy(orderBy)) {
            orderBy = "create_time";
        }
        if (!isValidOrderDirection(orderDirection)) {
            orderDirection = "DESC";
        }
        
        // 处理搜索关键词：为BOOLEAN MODE添加通配符支持
        // 将关键词按空格分割，每个词添加*通配符，实现前缀匹配
        String processedKeyword = processKeywordForBooleanMode(keyword);
        
        return searchMapper.search(processedKeyword, offset, size, orderBy, orderDirection, userId, isAdmin);
    }
    
    /**
     * 处理搜索关键词，为BOOLEAN MODE添加通配符
     * 例如："java spring" -> "+java* +spring*"
     */
    private String processKeywordForBooleanMode(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return "";
        }
        
        StringBuilder processed = new StringBuilder();
        String[] words = keyword.trim().split("\\s+");
        
        for (String word : words) {
            if (!word.isEmpty()) {
                // 移除可能存在的特殊字符，防止SQL注入
                String cleanWord = word.replaceAll("[^\\p{L}\\p{N}]", "");
                if (!cleanWord.isEmpty()) {
                    if (processed.length() > 0) {
                        processed.append(" ");
                    }
                    // 使用+表示必须包含，*表示通配符
                    processed.append("+").append(cleanWord).append("*");
                }
            }
        }
        
        return processed.toString();
    }

    /**
     * 验证排序字段是否合法
     */
    private boolean isValidOrderBy(String orderBy) {
        if (orderBy == null) return false;
        String lowerOrderBy = orderBy.toLowerCase();
        return lowerOrderBy.equals("create_time") || 
               lowerOrderBy.equals("title") || 
               lowerOrderBy.equals("id");
    }

    /**
     * 验证排序方向是否合法
     */
    private boolean isValidOrderDirection(String orderDirection) {
        if (orderDirection == null) return false;
        String upperDirection = orderDirection.toUpperCase();
        return upperDirection.equals("ASC") || upperDirection.equals("DESC");
    }
}