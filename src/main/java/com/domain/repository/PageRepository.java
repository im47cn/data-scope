package com.domain.repository;

import com.domain.model.lowcode.Page;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 页面仓储接口
 */
@Repository
public interface PageRepository {
    
    /**
     * 保存页面
     *
     * @param page 页面信息
     * @return 保存后的页面
     */
    Page save(Page page);
    
    /**
     * 根据ID查询页面
     *
     * @param id 页面ID
     * @return 页面信息
     */
    Optional<Page> findById(String id);
    
    /**
     * 根据应用ID查询页面列表
     *
     * @param appId 应用ID
     * @return 页面列表
     */
    List<Page> findByAppId(String appId);
    
    /**
     * 根据ID删除页面
     *
     * @param id 页面ID
     */
    void deleteById(String id);
    
    /**
     * 查询所有页面
     *
     * @return 页面列表
     */
    List<Page> findAll();
}