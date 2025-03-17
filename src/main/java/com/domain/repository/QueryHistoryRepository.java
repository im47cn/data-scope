package com.domain.repository;

import com.domain.model.query.QueryHistory;

import java.util.List;
import java.util.Optional;

/**
 * 查询历史仓储接口
 */
public interface QueryHistoryRepository {
    
    /**
     * 保存查询历史
     */
    QueryHistory save(QueryHistory queryHistory);
    
    /**
     * 根据ID查询
     */
    Optional<QueryHistory> findById(String id);
    
    /**
     * 根据数据源ID查询
     */
    List<QueryHistory> findByDataSourceId(String dataSourceId);
    
    /**
     * 根据数据源ID查询并按创建时间倒序排序
     */
    List<QueryHistory> findByDataSourceIdOrderByCreatedAtDesc(String dataSourceId);
    
    /**
     * 根据ID删除
     */
    void deleteById(String id);
    
    /**
     * 根据数据源ID删除
     */
    void deleteByDataSourceId(String dataSourceId);
    
    /**
     * 根据数据源ID和时间范围查询
     */
    List<QueryHistory> findByDataSourceIdAndTimeRange(String dataSourceId, String startTime, String endTime);
    
    /**
     * 根据数据源ID和SQL模式查询
     */
    List<QueryHistory> findByDataSourceIdAndSqlPattern(String dataSourceId, String sqlPattern);
    
    /**
     * 根据数据源ID和成功状态查询
     */
    List<QueryHistory> findByDataSourceIdAndSuccess(String dataSourceId, Boolean success);
}