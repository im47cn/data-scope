package com.domain.repository;

import com.domain.model.metadata.TableInfo;

import java.util.List;
import java.util.Optional;

/**
 * 表信息仓储接口
 */
public interface TableInfoRepository {
    
    /**
     * 保存表信息
     *
     * @param tableInfo 表信息
     * @return 保存后的表信息
     */
    TableInfo save(TableInfo tableInfo);
    
    /**
     * 根据ID查询表信息
     *
     * @param id 表ID
     * @return 表信息
     */
    Optional<TableInfo> findById(String id);
    
    /**
     * 根据数据源ID查询表信息列表
     *
     * @param dataSourceId 数据源ID
     * @return 表信息列表
     */
    List<TableInfo> findByDataSourceId(Long dataSourceId);
    
    /**
     * 根据数据源ID和Schema名称查询表信息列表
     *
     * @param dataSourceId 数据源ID
     * @param schemaName Schema名称
     * @return 表信息列表
     */
    List<TableInfo> findByDataSourceIdAndSchemaName(Long dataSourceId, String schemaName);
    
    /**
     * 删除表信息
     *
     * @param tableInfo 表信息
     */
    void delete(TableInfo tableInfo);
}