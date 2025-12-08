// ResourceService.java
package com.example.app.service;

import com.example.app.model.Resource;
import java.util.List;

public interface ResourceService {
    Resource getById(Long id);

    List<Resource> listAll();

    void create(Resource r);
}