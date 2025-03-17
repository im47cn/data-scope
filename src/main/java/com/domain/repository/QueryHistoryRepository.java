package com.domain.repository;

import com.domain.model.query.QueryHistory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 查询历史仓库
 * 使用MyBatis实现，不再使用JPA
 */
@Repository
public interface QueryHistoryRepository {
    
    /**
     * 保存查询历史
     * 
     * @param queryHistory 查询历史对象
     * @return 保存后的查询历史
     */
    QueryHistory save(QueryHistory queryHistory);
    
    /**
     * 根据ID查询查询历史
     * 
     * @param id 查询历史ID
     * @return 查询历史可选结果
     */
    Optional<QueryHistory> findById(String id);
    
    /**
     * 查询所有查询历史
     * 
     * @return 查询历史列表
     */
    List<QueryHistory> findAll();
    
    /**
     * 根据ID删除查询历史
     * 
     * @param id 查询历史ID
     */
    void deleteById(String id);
    
    /**
     * 根据数据源ID查询历史记录
     *
     * @param dataSourceId 数据源ID
     * @return 查询历史列表
     */
    List<QueryHistory> findByDataSourceIdOrderByCreatedAtDesc(String dataSourceId);
}