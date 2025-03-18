package com.insightdata.domain.service;

import com.insightdata.domain.metadata.model.SchemaInfo;
import com.insightdata.domain.metadata.model.TableRelationship;
import com.insightdata.domain.query.model.QueryHistory;

import java.util.List;

/**
 * 表关系服务接口
 * 负责表关系的学习、管理和查询
 */
public interface TableRelationshipService {

    /**
     * 获取获取指定ID的表关系
     *
     * @param id 表关系ID
     * @return 表关系
     */
    TableRelationship findById(String id);

    /**
     * 获取数据源的所有表关系
     *
     * @param dataSourceId 数据源ID
     * @return 表关系列表
     */
    List<TableRelationship> getAllTableRelationships(String dataSourceId);
    
    /**
     * 获取指定表之间的关系
     *
     * @param dataSourceId 数据源ID
     * @param tableNames 表名列表
     * @return 表关系列表
     */
    List<TableRelationship> getTableRelationships(String dataSourceId, List<String> tableNames);
    
    /**
     * 获取指定表的所有关系
     *
     * @param dataSourceId 数据源ID
     * @param tableName 表名
     * @return 表关系列表
     */
    List<TableRelationship> getTableRelationships(String dataSourceId, String tableName);
    
    /**
     * 从元数据中学习表关系
     * 主要基于外键约束
     *
     * @param dataSourceId 数据源ID
     * @param schemaInfo 数据库模式信息
     * @return 学习到的表关系列表
     */
    List<TableRelationship> learnFromMetadata(String dataSourceId, SchemaInfo schemaInfo);
    
    /**
     * 从查询历史中学习表关系
     * 分析JOIN语句和WHERE条件
     *
     * @param dataSourceId 数据源ID
     * @param queryHistories 查询历史列表
     * @return 学习到的表关系列表
     */
    List<TableRelationship> learnFromQueryHistory(String dataSourceId, List<QueryHistory> queryHistories);
    
    /**
     * 从用户反馈中学习表关系
     *
     * @param dataSourceId 数据源ID
     * @param sourceTable 源表
     * @param sourceColumn 源列
     * @param targetTable 目标表
     * @param targetColumn 目标列
     * @param type 关系类型
     * @return 创建的表关系
     */
    TableRelationship learnFromUserFeedback(String dataSourceId, String sourceTable, String sourceColumn,
                                          String targetTable, String targetColumn, 
                                          TableRelationship.RelationshipType type);
    
    /**
     * 保存表关系
     *
     * @param relationship 表关系
     * @return 保存的表关系
     */
    TableRelationship saveTableRelationship(TableRelationship relationship);
    
    /**
     * 删除表关系
     *
     * @param relationshipId 关系ID
     */
    void deleteTableRelationship(String relationshipId);
    
    /**
     * 更新表关系权重
     * 当关系被成功使用时，增加权重
     *
     * @param relationshipId 关系ID
     * @param weightDelta 权重变化值
     * @return 更新后的表关系
     */
    TableRelationship updateRelationshipWeight(String relationshipId, double weightDelta);
    
    /**
     * 推荐表关系
     * 基于现有关系和使用频率，推荐可能的表关系
     *
     * @param dataSourceId 数据源ID
     * @param tableName 表名
     * @param limit 限制数量
     * @return 推荐的表关系列表
     */
    List<TableRelationship> recommendRelationships(String dataSourceId, String tableName, int limit);
}