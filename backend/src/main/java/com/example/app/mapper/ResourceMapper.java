// ResourceMapper.java
package com.example.app.mapper;

import com.example.app.model.Resource;
import java.util.List;

public interface ResourceMapper {
    Resource selectById(Long id);

    List<Resource> selectAll();

    void insert(Resource r);
}