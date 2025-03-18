package com.insightdata.domain.repository;

import com.insightdata.domain.query.model.SavedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedQueryRepository {
    /**
     * 保存查询模板
     */
    SavedQuery save(SavedQuery savedQuery);
    
    /**
     * 根据ID查找查询模板
     */
    Optional<SavedQuery> findById(String id);
    
    /**
     * 根据数据源ID和用户ID查找查询模板
     */
    List<SavedQuery> findByDataSourceIdAndUserId(String dataSourceId, String userId);
    
    /**
     * 根据ID删除查询模板
     */
    void deleteById(String id);
    
    /**
     * 根据标签查找查询模板
     */
    List<SavedQuery> findByTags(List<String> tags);
    
    /**
     * 根据名称模糊查找查询模板
     */
    List<SavedQuery> findByNameLike(String name);
    
    /**
     * 更新查询模板的使用统计信息
     */
    void updateUsageStats(String id, long executionTime);
    
    /**
     * 根据数据源ID查找查询模板
     */
    List<SavedQuery> findByDataSourceId(String dataSourceId);
    
    /**
     * 检查指定名称的查询模板是否存在
     */
    boolean existsByName(String name);
    
    /**
     * 根据数据源ID删除所有查询模板
     */
    void deleteByDataSourceId(String dataSourceId);
}