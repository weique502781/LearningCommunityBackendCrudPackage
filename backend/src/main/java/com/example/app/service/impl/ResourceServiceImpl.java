// ResourceServiceImpl.java-用于实现资源服务接口
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
    private ResourceMapper resourceMapper;//资源数据访问对象

    // 根据ID获取资源
    public Resource getById(Long id) {
        return resourceMapper.selectById(id);
    }

    // 获取所有资源
    public List<Resource> listAll() {
        return resourceMapper.selectAll();
    }

    // 创建新资源
    public void create(Resource r) {
        resourceMapper.insert(r);
    }
}