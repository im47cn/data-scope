package com.insightdata.domain.repository;

import com.insightdata.domain.model.lowcode.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 组件仓储接口
 */
@Repository
public interface ComponentRepository {
    
    /**
     * 保存组件
     *
     * @param component 组件信息
     * @return 保存后的组件
     */
    Component save(Component component);
    
    /**
     * 根据ID查询组件
     *
     * @param id 组件ID
     * @return 组件信息
     */
    Optional<Component> findById(Long id);
    
    /**
     * 根据页面ID查询组件列表
     *
     * @param pageId 页面ID
     * @return 组件列表
     */
    List<Component> findByPageId(Long pageId);
    
    /**
     * 根据ID删除组件
     *
     * @param id 组件ID
     */
    void deleteById(Long id);
    
    /**
     * 查询所有组件
     *
     * @return 组件列表
     */
    List<Component> findAll();
    
    /**
     * 根据类型查询组件列表
     *
     * @param type 组件类型
     * @return 组件列表
     */
    List<Component> findByType(String type);
    
    /**
     * 根据页面ID删除所有组件
     *
     * @param pageId 页面ID
     */
    void deleteByPageId(Long pageId);
}