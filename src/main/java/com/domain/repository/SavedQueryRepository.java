package com.domain.repository;

import com.domain.model.query.SavedQuery;

import java.util.List;
import java.util.Optional;

/**
 * 保存的查询仓储接口
 */
public interface SavedQueryRepository {
    
    /**
     * 保存查询
     */
    SavedQuery save(SavedQuery savedQuery);
    
    /**
     * 根据ID查询
     */
    Optional<SavedQuery> findById(String id);
    
    /**
     * 根据数据源ID查询
     */
    List<SavedQuery> findByDataSourceId(String dataSourceId);
    
    /**
     * 根据ID删除
     */
    void deleteById(String id);
    
    /**
     * 根据数据源ID删除
     */
    void deleteByDataSourceId(String dataSourceId);
    
    /**
     * 根据名称模糊查询
     */
    List<SavedQuery> findByNameLike(String name);
    
    /**
     * 根据标签查询
     */
    List<SavedQuery> findByTags(List<String> tags);
    
    /**
     * 根据是否公开查询
     */
    List<SavedQuery> findByIsPublic(Boolean isPublic);
    
    /**
     * 根据数据源ID和是否公开查询
     */
    List<SavedQuery> findByDataSourceIdAndIsPublic(String dataSourceId, Boolean isPublic);
}