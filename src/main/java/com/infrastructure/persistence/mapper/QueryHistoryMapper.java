package com.infrastructure.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.domain.model.query.QueryHistory;

@Mapper
public interface QueryHistoryMapper {
    
    /**
     * 插入查询历史
     */
    void insert(QueryHistory queryHistory);
    
    /**
     * 更新查询历史
     */
    void update(QueryHistory queryHistory);
    
    /**
     * 根据ID查询
     */
    QueryHistory selectById(@Param("id") String id);
    
    /**
     * 根据数据源ID查询
     */
    List<QueryHistory> selectByDataSourceId(@Param("dataSourceId") String dataSourceId);
    
    /**
     * 根据数据源ID查询并按创建时间倒序排序
     */
    List<QueryHistory> selectByDataSourceIdOrderByCreatedAtDesc(@Param("dataSourceId") String dataSourceId);
    
    /**
     * 根据ID删除
     */
    void deleteById(@Param("id") String id);
    
    /**
     * 根据数据源ID删除
     */
    void deleteByDataSourceId(@Param("dataSourceId") String dataSourceId);
    
    /**
     * 根据数据源ID和时间范围查询
     */
    List<QueryHistory> selectByDataSourceIdAndTimeRange(
            @Param("dataSourceId") String dataSourceId,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime);
    
    /**
     * 根据数据源ID和SQL模式查询
     */
    List<QueryHistory> selectByDataSourceIdAndSqlPattern(
            @Param("dataSourceId") String dataSourceId,
            @Param("sqlPattern") String sqlPattern);
    
    /**
     * 根据数据源ID和成功状态查询
     */
    List<QueryHistory> selectByDataSourceIdAndSuccess(
            @Param("dataSourceId") String dataSourceId,
            @Param("success") Boolean success);
}