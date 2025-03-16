package com.insightdata.domain.service;

import com.insightdata.domain.model.metadata.SchemaInfo;
import com.insightdata.domain.model.metadata.TableRelationship;

import java.util.List;

/**
 * 表关系推断服务接口
 * 负责从不同数据源推断表之间的关系
 */
public interface TableRelationshipInferenceService {
    
    /**
     * 根据列内容相似性推断表关系
     * 
     * @param dataSourceId 数据源ID
     * @param schemaInfo 数据库模式信息
     * @return 推断出的表关系列表
     */
    List<TableRelationship> inferRelationshipsFromContent(Long dataSourceId, SchemaInfo schemaInfo);
    
    /**
     * 根据列名相似性推断表关系
     * 
     * @param dataSourceId 数据源ID
     * @param schemaInfo 数据库模式信息
     * @return 推断出的表关系列表
     */
    List<TableRelationship> inferRelationshipsFromColumnNames(Long dataSourceId, SchemaInfo schemaInfo);
    
    /**
     * 根据常见模式推断表关系
     * 例如：外键命名约定、多对多关系表等
     * 
     * @param dataSourceId 数据源ID
     * @param schemaInfo 数据库模式信息
     * @return 推断出的表关系列表
     */
    List<TableRelationship> inferRelationshipsFromCommonPatterns(Long dataSourceId, SchemaInfo schemaInfo);
    
    /**
     * 综合多种方法推断表关系
     * 
     * @param dataSourceId 数据源ID
     * @param schemaInfo 数据库模式信息
     * @return 推断出的表关系列表
     */
    List<TableRelationship> inferRelationships(Long dataSourceId, SchemaInfo schemaInfo);
    
    /**
     * 为特定表推荐可能的关系
     * 
     * @param dataSourceId 数据源ID
     * @param tableName 表名
     * @param confidence 最低置信度阈值
     * @param limit 最大推荐数量
     * @return 推荐的表关系列表
     */
    List<TableRelationship> recommendRelationships(Long dataSourceId, String tableName, double confidence, int limit);
}