package com.domain.repository;

import com.domain.model.lowcode.App;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 低代码应用仓储接口
 */
@Repository
public interface AppRepository {
    
    /**
     * 保存应用
     *
     * @param app 应用信息
     * @return 保存后的应用
     */
    App save(App app);
    
    /**
     * 根据ID查询应用
     *
     * @param id 应用ID
     * @return 应用信息
     */
    Optional<App> findById(String id);
    
    /**
     * 根据应用编码查询应用
     *
     * @param code 应用编码
     * @return 应用信息
     */
    Optional<App> findByCode(String code);
    
    /**
     * 查询所有应用
     *
     * @return 应用列表
     */
    List<App> findAll();
    
    /**
     * 查询所有已发布的应用
     *
     * @return 已发布的应用列表
     */
    List<App> findPublished();
    
    /**
     * 根据名称模糊查询应用
     *
     * @param name 应用名称
     * @return 应用列表
     */
    List<App> findByNameContaining(String name);
    
    /**
     * 检查应用编码是否已存在
     *
     * @param code 应用编码
     * @return 是否存在
     */
    boolean existsByCode(String code);
    
    /**
     * 根据ID删除应用
     *
     * @param id 应用ID
     */
    void deleteById(String id);
}