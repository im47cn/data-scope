package com.insightdata.domain.model.metadata;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表信息模型
 * 用于表示数据库表的结构信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableInfo {

    private Long id;
    
    /**
     * 表名
     */
    private String name;
    
    /**
     * 表描述
     */
    private String description;
    
    /**
     * 表类型（TABLE, VIEW, SYSTEM_TABLE等）
     */
    private String type;
    
    /**
     * 所属模式（Schema）名称
     */
    private String schemaName;
    
    /**
     * 所属数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 行数（估计值）
     */
    private Long rowCount;

    private Long dataSize;

    private Long indexSize;

    /**
     * 列信息列表
     */
    private List<ColumnInfo> columns;
    
    /**
     * 索引信息列表
     */
    private List<IndexInfo> indexes;
    
    /**
     * 外键信息列表
     */
    private List<ForeignKeyInfo> foreignKeys;
    
    /**
     * 表统计信息
     */
    private Map<String, Object> statistics;
    
    /**
     * 添加列信息
     * 
     * @param column 列信息
     */
    public void addColumn(ColumnInfo column) {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        if (column != null) {
            columns.add(column);
        }
    }
    
    /**
     * 添加索引信息
     * 
     * @param index 索引信息
     */
    public void addIndex(IndexInfo index) {
        if (indexes == null) {
            indexes = new ArrayList<>();
        }
        if (index != null) {
            indexes.add(index);
        }
    }
    
    /**
     * 添加外键信息
     * 
     * @param foreignKey 外键信息
     */
    public void addForeignKey(ForeignKeyInfo foreignKey) {
        if (foreignKeys == null) {
            foreignKeys = new ArrayList<>();
        }
        if (foreignKey != null) {
            foreignKeys.add(foreignKey);
        }
    }
    
    /**
     * 添加表统计信息
     * 
     * @param key 统计名称
     * @param value 统计值
     */
    public void addStatistic(String key, Object value) {
        if (statistics == null) {
            statistics = new HashMap<>();
        }
        if (key != null) {
            statistics.put(key, value);
        }
    }
    
    /**
     * 获取指定名称的列信息
     * 
     * @param columnName 列名
     * @return 列信息（如果存在）
     */
    public Optional<ColumnInfo> getColumn(String columnName) {
        if (columns == null || columnName == null) {
            return Optional.empty();
        }
        
        return columns.stream()
                .filter(column -> columnName.equalsIgnoreCase(column.getName()))
                .findFirst();
    }
    
    /**
     * 获取主键列列表
     * 
     * @return 主键列列表
     */
    public List<ColumnInfo> getPrimaryKeyColumns() {
        if (columns == null) {
            return Collections.emptyList();
        }
        
        return columns.stream()
                .filter(ColumnInfo::isPrimaryKey)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取主键列名列表
     * 
     * @return 主键列名列表
     */
    public List<String> getPrimaryKeyColumnNames() {
        return getPrimaryKeyColumns().stream()
                .map(ColumnInfo::getName)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取外键列列表
     * 
     * @return 外键列列表
     */
    public List<ColumnInfo> getForeignKeyColumns() {
        if (columns == null) {
            return Collections.emptyList();
        }
        
        return columns.stream()
                .filter(ColumnInfo::isForeignKey)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取指定表名的外键列表
     * 
     * @param targetTableName 目标表名
     * @return 指向指定表的外键列表
     */
    public List<ForeignKeyInfo> getForeignKeysToTable(String targetTableName) {
        if (foreignKeys == null || targetTableName == null) {
            return Collections.emptyList();
        }
        
        return foreignKeys.stream()
                .filter(fk -> targetTableName.equalsIgnoreCase(fk.getTargetTableName()))
                .collect(Collectors.toList());
    }
    
    /**
     * 是否为视图
     * 
     * @return 如果是视图返回true，否则返回false
     */
    public boolean isView() {
        if (type == null) {
            return false;
        }
        return type.toUpperCase().contains("VIEW");
    }
    
    /**
     * 获取索引列的列名列表
     * 
     * @return 所有索引列的列名集合
     */
    public List<String> getIndexedColumnNames() {
        if (indexes == null) {
            return Collections.emptyList();
        }
        
        return indexes.stream()
                .flatMap(index -> index.getColumnNames().stream())
                .distinct()
                .collect(Collectors.toList());
    }
    
    /**
     * 获取完整的表标识
     * 
     * @return 格式为"schema.table"的完整表标识
     */
    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if (schemaName != null && !schemaName.isEmpty()) {
            sb.append(schemaName).append(".");
        }
        if (name != null) {
            sb.append(name);
        }
        return sb.toString();
    }
}