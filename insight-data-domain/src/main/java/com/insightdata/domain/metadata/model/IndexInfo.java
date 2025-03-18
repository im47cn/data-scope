package com.insightdata.domain.metadata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 索引信息实体类
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class IndexInfo {

    /**
     * 索引ID，使用UUID
     */
    private String id;
    
    /**
     * 表ID
     */
    private String tableId;
    
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 索引名称
     */
    private String name;
    
    /**
     * 表名
     */
    private String tableName;
    
    /**
     * 模式名
     */
    private String schemaName;
    
    /**
     * 是否唯一索引
     */
    private Boolean isUnique;
    
    /**
     * 是否是主键索引
     */
    private Boolean isPrimary;
    
    /**
     * 索引类型（如BTREE、HASH等）
     */
    private String type;
    
    /**
     * 索引方法（如btree、gin、gist等）
     */
    private String method;
    
    /**
     * 过滤条件（用于部分索引）
     */
    private String filterCondition;
    
    /**
     * 索引选项
     */
    private String options;
    
    /**
     * 索引列列表
     */
    private List<IndexColumnInfo> columns = new ArrayList<>();
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 创建者
     */
    private String createdBy;
    
    /**
     * 更新者
     */
    private String updatedBy;
    
    /**
     * 获取索引的所有列名
     * 
     * @return 列名列表
     */
    public List<String> getColumnNames() {
        if (columns == null || columns.isEmpty()) {
            return new ArrayList<>();
        }
        
        return columns.stream()
                .map(IndexColumnInfo::getColumnName)
                .collect(Collectors.toList());
    }
    
    /**
     * 检查索引是否包含指定列
     * 
     * @param columnName 列名
     * @return 是否包含
     */
    public boolean containsColumn(String columnName) {
        if (columns == null || columns.isEmpty()) {
            return false;
        }
        
        return columns.stream()
                .anyMatch(column -> column.getColumnName().equalsIgnoreCase(columnName));
    }
    
    /**
     * 获取索引的降序列名列表
     * 
     * @return 降序列名列表
     */
    public List<String> getDescendingColumnNames() {
        if (columns == null || columns.isEmpty()) {
            return new ArrayList<>();
        }
        
        return columns.stream()
                .filter(column -> !column.isAscending())
                .map(IndexColumnInfo::getColumnName)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取指定位置的列信息
     * 
     * @param position 位置（从1开始）
     * @return 索引列信息
     */
    public IndexColumnInfo getColumnAtPosition(int position) {
        if (columns == null || columns.isEmpty()) {
            return null;
        }
        
        return columns.stream()
                .filter(column -> column.getPosition() == position)
                .findFirst()
                .orElse(null);
    }
    
    /**
     * 检查索引是否是简单索引（只有一列）
     * 
     * @return 是否是简单索引
     */
    public boolean isSimpleIndex() {
        return columns != null && columns.size() == 1;
    }
    
    /**
     * 获取索引列数量
     * 
     * @return 列数量
     */
    public int getColumnCount() {
        return columns != null ? columns.size() : 0;
    }
}