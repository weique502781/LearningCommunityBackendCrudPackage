package com.example.app.controller;

import com.example.app.model.Question;
import com.example.app.service.SearchService;
import com.example.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    SearchController.java
    处理搜索相关的HTTP请求
*/
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;//处理搜索相关操作

    @Autowired
    private UserService userService;//用户权限验证

    /**
     * 搜索问题接口
     * @param keyword 搜索关键词（必填）
     * @param page 页码，从1开始（必填）
     * @param size 每页大小（必填）
     * @param orderBy 排序字段（可选：create_time, title, id，默认为create_time）
     * @param orderDirection 排序方向（可选：ASC, DESC，默认为DESC）
     * @return 包含问题列表的响应
     */
    @GetMapping
    public Map<String, Object> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDirection,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        
        boolean isAdmin = false;
        if (userId != null) {
            isAdmin = userService.isAdmin(userId);
        }

        List<Question> questions = searchService.search(keyword, page, size, orderBy, orderDirection, userId, isAdmin);
        
        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        result.put("data", questions);
        result.put("page", page);
        result.put("size", size);
        result.put("total", questions.size());
        return result;
    }
}