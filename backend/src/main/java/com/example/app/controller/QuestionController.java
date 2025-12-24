package com.example.app.controller;

import com.example.app.model.Question;
import com.example.app.service.QuestionService;
import com.example.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    QuestionController.java
    处理问题相关的HTTP请求
*/
@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;//处理问题相关操作

    @Autowired
    private UserService userService;//用户权限验证

    // 获取所有可见问题，包含权限检查
    @GetMapping
    public List<Question> list(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        boolean isAdmin = false;
        if (userId != null) {
            isAdmin = userService.isAdmin(userId);
        }
        return questionService.listVisible(userId, isAdmin);
    }

    // 创建新问题，管理员自动批准
    @PostMapping
    public Map<String, Object> create(@RequestBody Question q,
                                      @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId != null && userService.isAdmin(userId)) {
            q.setStatus("APPROVED");
        }
        questionService.create(q);
        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        return result;
    }

    //通过ID获取问题，包含权限检查
    @GetMapping("/{id}")
    public ResponseEntity<Question> get(@PathVariable Long id,
                                        @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        Question q = questionService.getById(id);
        if (q == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        boolean isAdmin = false;
        if (userId != null) {
            isAdmin = userService.isAdmin(userId);
        }

        if (isAdmin || "APPROVED".equals(q.getStatus()) || (userId != null && userId.equals(q.getUserId()))) {
            return ResponseEntity.ok(q);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}