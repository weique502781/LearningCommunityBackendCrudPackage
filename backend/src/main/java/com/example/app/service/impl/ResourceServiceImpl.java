// ResourceServiceImpl.java
package com.example.app.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.app.service.ResourceService;
import com.example.app.mapper.ResourceMapper;
import com.example.app.model.Resource;
import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    public Resource getById(Long id) {
        return resourceMapper.selectById(id);
    }

    public List<Resource> listAll() {
        return resourceMapper.selectAll();
    }

    public void create(Resource r) {
        resourceMapper.insert(r);
    }
}