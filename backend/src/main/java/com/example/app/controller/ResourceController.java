package com.example.app.controller;

import com.example.app.model.Resource;
import com.example.app.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    ResourceController.java
    处理资源相关的HTTP请求
*/
@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;//处理资源相关操作

    // 获取所有资源
    @GetMapping
    public List<Resource> list() {
        return resourceService.listAll();
    }

    // 创建新资源
    @PostMapping
    public Map<String, Object> create(@RequestBody Resource r) {
        resourceService.create(r);
        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        return result;
    }
}