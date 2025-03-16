package com.insightdata.domain.model.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 外键信息模型
 * 用于表示数据库表中外键约束的结构信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForeignKeyInfo {
    
    /**
     * 外键名称
     */
    private String name;
    
    /**
     * 外键所属的表（源表）
     */
    private String sourceTableName;
    
    /**
     * 外键引用的表（目标表）
     */
    private String targetTableName;
    
    /**
     * 所属模式（Schema）名称
     */
    private String schemaName;
    
    /**
     * 目标表的模式（Schema）名称
     */
    private String targetSchemaName;
    
    /**
     * 所属数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 更新规则（CASCADE, SET NULL, RESTRICT等）
     */
    private String updateRule;
    
    /**
     * 删除规则（CASCADE, SET NULL, RESTRICT等）
     */
    private String deleteRule;
    
    /**
     * 延迟规则（INITIALLY DEFERRED, INITIALLY IMMEDIATE等）
     */
    private String deferrability;
    
    /**
     * 外键列信息列表
     */
    private List<ForeignKeyColumnInfo> columns;
    
    /**
     * 添加外键列映射
     * 
     * @param column 外键列信息
     */
    public void addColumn(ForeignKeyColumnInfo column) {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        if (column != null) {
            columns.add(column);
        }
    }
    
    /**
     * 获取源表列名列表
     * 
     * @return 源表列名列表
     */
    public List<String> getSourceColumnNames() {
        if (columns == null) {
            return new ArrayList<>();
        }
        
        return columns.stream()
                .map(ForeignKeyColumnInfo::getSourceColumnName)
                .toList();
    }
    
    /**
     * 获取目标表列名列表
     * 
     * @return 目标表列名列表
     */
    public List<String> getTargetColumnNames() {
        if (columns == null) {
            return new ArrayList<>();
        }
        
        return columns.stream()
                .map(ForeignKeyColumnInfo::getTargetColumnName)
                .toList();
    }
    
    /**
     * 检查外键是否包含指定源列
     * 
     * @param columnName 列名
     * @return 如果外键包含指定源列返回true，否则返回false
     */
    public boolean containsSourceColumn(String columnName) {
        if (columns == null || columnName == null) {
            return false;
        }
        
        return columns.stream()
                .anyMatch(column -> columnName.equalsIgnoreCase(column.getSourceColumnName()));
    }
    
    /**
     * 检查外键是否包含指定目标列
     * 
     * @param columnName 列名
     * @return 如果外键包含指定目标列返回true，否则返回false
     */
    public boolean containsTargetColumn(String columnName) {
        if (columns == null || columnName == null) {
            return false;
        }
        
        return columns.stream()
                .anyMatch(column -> columnName.equalsIgnoreCase(column.getTargetColumnName()));
    }
}