// ResourceController.java
package com.example.app.controller;

import com.example.app.model.Resource;
import com.example.app.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping
    public List<Resource> list() {
        return resourceService.listAll();
    }

    @PostMapping
    public Map<String, Object> create(@RequestBody Resource r) {
        resourceService.create(r);
        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        return result;
    }
}