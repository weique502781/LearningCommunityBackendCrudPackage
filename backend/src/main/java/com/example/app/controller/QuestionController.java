// QuestionController.java
package com.example.app.controller;

import com.example.app.model.Question;
import com.example.app.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping
    public List<Question> list() {
        return questionService.listAll();
    }

    @PostMapping
    public Map<String, Object> create(@RequestBody Question q) {
        questionService.create(q);
        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        return result;
    }

    @GetMapping("/{id}")
    public Question get(@PathVariable Long id) {
        return questionService.getById(id);
    }
}