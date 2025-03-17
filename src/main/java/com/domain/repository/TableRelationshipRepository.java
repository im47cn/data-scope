package com.domain.repository;

import com.domain.model.metadata.TableRelationship;

import java.util.List;
import java.util.Optional;

/**
 * 表关系存储库接口
 */
public interface TableRelationshipRepository {
    
    /**
     * 保存表关系
     * 
     * @param relationship 要保存的表关系
     * @return 保存后的表关系（包含ID）
     */
    TableRelationship save(TableRelationship relationship);
    
    /**
     * 批量保存表关系
     * 
     * @param relationships 要保存的表关系列表
     * @return 保存后的表关系列表
     */
    List<TableRelationship> saveAll(List<TableRelationship> relationships);
    
    /**
     * 根据ID查找表关系
     * 
     * @param id 表关系ID
     * @return 表关系（如果存在）
     */
    Optional<TableRelationship> findById(String id);
    
    /**
     * 根据数据源ID查找所有表关系
     * 
     * @param dataSourceId 数据源ID
     * @return 表关系列表
     */
    List<TableRelationship> findByDataSourceId(String dataSourceId);
    
    /**
     * 查找特定表的所有关系
     * 
     * @param dataSourceId 数据源ID
     * @param tableName 表名
     * @return 表关系列表
     */
    List<TableRelationship> findByDataSourceIdAndTable(String dataSourceId, String tableName);
    
    /**
     * 查找两个表之间的所有关系
     * 
     * @param dataSourceId 数据源ID
     * @param sourceTable 源表名
     * @param targetTable 目标表名
     * @return 表关系列表
     */
    List<TableRelationship> findByDataSourceIdAndTables(String dataSourceId, String sourceTable, String targetTable);
    
    /**
     * 根据ID删除表关系
     * 
     * @param id 表关系ID
     */
    void deleteById(String id);
    
    /**
     * 删除数据源的所有表关系
     * 
     * @param dataSourceId 数据源ID
     */
    void deleteByDataSourceId(String dataSourceId);
    
    /**
     * 通过学习来源删除数据源的表关系
     * 
     * @param dataSourceId 数据源ID
     * @param source 关系来源
     */
    void deleteByDataSourceIdAndSource(String dataSourceId, TableRelationship.RelationshipSource source);
    
    /**
     * 更新表关系使用频率
     * 
     * @param id 表关系ID
     */
    void incrementFrequency(String id);
}