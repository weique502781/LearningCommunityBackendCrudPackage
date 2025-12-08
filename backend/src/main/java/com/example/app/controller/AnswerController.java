// AnswerController.java
package com.example.app.controller;

import com.example.app.model.Answer;
import com.example.app.service.AnswerService;
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
    private StringRedisTemplate redisTemplate;

    @GetMapping("/question/{qid}")
    public List<Answer> listByQuestion(@PathVariable("qid") Long qid) {
        return answerService.listByQuestion(qid);
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
}