package com.insightdata.domain.metadata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库模式信息
 * 包含数据库中的表、列和关系信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchemaInfo {
    
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 模式名称
     */
    private String name;
    
    /**
     * 表信息列表
     */
    private List<TableInfo> tables = new ArrayList<>();
    
    /**
     * 表之间的关系
     */
    private List<TableRelationship> relationships = new ArrayList<>();
    
    /**
     * 附加属性
     */
    private Map<String, Object> properties = new HashMap<>();
    
    /**
     * 添加表信息
     */
    public void addTable(TableInfo table) {
        if (tables == null) {
            tables = new ArrayList<>();
        }
        tables.add(table);
    }
    
    /**
     * 添加表关系
     */
    public void addRelationship(TableRelationship relationship) {
        if (relationships == null) {
            relationships = new ArrayList<>();
        }
        relationships.add(relationship);
    }
    
    /**
     * 根据表名查找表信息
     */
    public TableInfo findTableByName(String tableName) {
        if (tables == null || tableName == null) {
            return null;
        }
        
        return tables.stream()
                .filter(table -> tableName.equals(table.getName()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * 查找两个表之间的关系
     */
    public List<TableRelationship> findRelationships(String sourceTable, String targetTable) {
        if (relationships == null || sourceTable == null || targetTable == null) {
            return new ArrayList<>();
        }
        
        List<TableRelationship> result = new ArrayList<>();
        for (TableRelationship relationship : relationships) {
            if ((sourceTable.equals(relationship.getSourceTableName()) && targetTable.equals(relationship.getTargetTableName())) ||
                (sourceTable.equals(relationship.getTargetTableName()) && targetTable.equals(relationship.getSourceTableName()))) {
                result.add(relationship);
            }
        }
        
        return result;
    }
}