// ResourceMapper.java
package com.example.app.mapper;

import com.example.app.model.Resource;
import java.util.List;

public interface ResourceMapper {
    Resource selectById(Long id);//根据ID获取资源
    List<Resource> selectAll();//获取所有资源
    List<Resource> selectByUserId(Long userId);  // 按用户ID查询
    List<Resource> selectByStatus(String status);  // 按状态查询
    void insert(Resource r);//插入新资源
    void updateStatus(Long id, String status);  // 更新状态
    Long countResources();  // 统计资源数量
    Long countResourcesByStatus(String status);  // 按状态统计
}