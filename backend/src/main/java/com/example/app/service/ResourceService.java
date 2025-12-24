// ResourceService.java-用于定义资源服务接口，包含获取、列出和创建资源的方法。
package com.example.app.service;

import com.example.app.model.Resource;
import java.util.List;

public interface ResourceService {
    Resource getById(Long id);//根据ID获取资源

    List<Resource> listAll();//获取所有资源

    void create(Resource r);//创建新资源
}