package com.insightdata.domain.repository;

import com.insightdata.domain.model.lowcode.App;
import java.util.List;
import java.util.Optional;

/**
 * 低代码应用仓库接口
 */
public interface AppRepository {
    
    /**
     * 保存应用
     * 
     * @param app 应用
     * @return 保存后的应用
     */
    App save(App app);
    
    /**
     * 根据ID查询应用
     * 
     * @param id 应用ID
     * @return 应用
     */
    Optional<App> findById(Long id);
    
    /**
     * 根据编码查询应用
     * 
     * @param code 应用编码
     * @return 应用
     */
    Optional<App> findByCode(String code);
    
    /**
     * 查询所有应用
     * 
     * @return 应用列表
     */
    List<App> findAll();
    
    /**
     * 查询已发布的应用
     * 
     * @return 已发布的应用列表
     */
    List<App> findPublished();
    
    /**
     * 根据ID删除应用
     * 
     * @param id 应用ID
     */
    void deleteById(Long id);
    
    /**
     * 根据名称模糊查询应用
     * 
     * @param name 应用名称
     * @return 应用列表
     */
    List<App> findByNameContaining(String name);
    
    /**
     * 检查编码是否已存在
     * 
     * @param code 应用编码
     * @return 是否存在
     */
    boolean existsByCode(String code);
}