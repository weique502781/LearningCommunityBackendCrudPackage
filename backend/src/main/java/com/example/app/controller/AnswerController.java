// AnswerController.java
package com.example.app.controller;

import com.example.app.model.Answer;
import com.example.app.service.AnswerService;
import com.example.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/question/{qid}")
    public List<Answer> listByQuestion(@PathVariable("qid") Long qid,
                                       @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        boolean isAdmin = false;
        if (userId != null) {
            isAdmin = userService.isAdmin(userId);
        }
        return answerService.listVisibleByQuestion(qid, userId, isAdmin);
    }

    @PostMapping
    public Map<String, Object> create(@RequestBody Answer a) {
        answerService.create(a);
        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        return result;
    }

    // 点赞回答
    @PostMapping("/{answerId}/like")
    public Map<String, Object> likeAnswer(@PathVariable("answerId") Long answerId,
                                          @RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        try {
            answerService.like(answerId, userId);
            Map<String, Object> result = new HashMap<>();
            result.put("ok", true);
            result.put("message", "点赞成功");
            return result;
        } catch (RuntimeException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("ok", false);
            result.put("error", e.getMessage());
            return result;
        }
    }

    // 获取点赞数
    @GetMapping("/{answerId}/like-count")
    public Map<String, Object> getLikeCount(@PathVariable("answerId") Long answerId) {
        String countKey = "answer:like:count:" + answerId;
        String count = redisTemplate.opsForValue().get(countKey);

        Map<String, Object> result = new HashMap<>();
        result.put("answerId", answerId);
        result.put("likeCount", count != null ? Integer.parseInt(count) : 0);
        return result;
    }

    // 检查用户是否已点赞
    @GetMapping("/{answerId}/liked/{userId}")
    public Map<String, Object> checkLiked(@PathVariable("answerId") Long answerId,
                                          @PathVariable("userId") Long userId) {
        String key = "answer:like:set:" + answerId;
        Boolean isLiked = redisTemplate.opsForSet().isMember(key, userId.toString());

        Map<String, Object> result = new HashMap<>();
        result.put("answerId", answerId);
        result.put("userId", userId);
        result.put("isLiked", isLiked != null && isLiked);
        return result;
    }

    // 标记最佳答案
    @PostMapping("/{answerId}/mark-best")
    public Map<String, Object> markAsBest(@PathVariable("answerId") Long answerId,
                                          @RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        try {
            answerService.markAsBest(answerId, userId);
            Map<String, Object> result = new HashMap<>();
            result.put("ok", true);
            result.put("message", "已标记为最佳答案");
            return result;
        } catch (RuntimeException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("ok", false);
            result.put("error", e.getMessage());
            return result;
        }
    }

    // 取消最佳答案标记
    @PostMapping("/{answerId}/unmark-best")
    public Map<String, Object> unmarkAsBest(@PathVariable("answerId") Long answerId,
                                            @RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        try {
            answerService.unmarkAsBest(answerId, userId);
            Map<String, Object> result = new HashMap<>();
            result.put("ok", true);
            result.put("message", "已取消最佳答案标记");
            return result;
        } catch (RuntimeException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("ok", false);
            result.put("error", e.getMessage());
            return result;
        }
    }

    // 获取问题的最佳答案
    @GetMapping("/question/{qid}/best")
    public Map<String, Object> getBestAnswer(@PathVariable("qid") Long questionId) {
        try {
            Answer bestAnswer = answerService.getBestAnswer(questionId);
            Map<String, Object> result = new HashMap<>();
            result.put("ok", true);
            result.put("bestAnswer", bestAnswer);
            return result;
        } catch (RuntimeException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("ok", false);
            result.put("error", e.getMessage());
            return result;
        }
    }

    // 检查用户是否有权限标记最佳答案
    @GetMapping("/question/{qid}/can-mark-best/{userId}")
    public Map<String, Object> canMarkBestAnswer(@PathVariable("qid") Long questionId,
                                                 @PathVariable("userId") Long userId) {
        try {
            boolean canMark = answerService.canMarkBestAnswer(userId, questionId);
            Map<String, Object> result = new HashMap<>();
            result.put("ok", true);
            result.put("canMark", canMark);
            result.put("questionId", questionId);
            result.put("userId", userId);
            return result;
        } catch (RuntimeException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("ok", false);
            result.put("error", e.getMessage());
            return result;
        }
    }
}