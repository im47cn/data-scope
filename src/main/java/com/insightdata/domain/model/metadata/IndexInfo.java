package com.insightdata.domain.model.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 索引信息模型
 * 用于表示数据库表索引的结构信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexInfo {
    
    /**
     * 索引名称
     */
    private String name;
    
    /**
     * 索引类型（BTREE, HASH等）
     */
    private String type;
    
    /**
     * 所属表名
     */
    private String tableName;
    
    /**
     * 所属模式（Schema）名称
     */
    private String schemaName;
    
    /**
     * 所属数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 是否是唯一索引
     */
    private Boolean unique;
    
    /**
     * 是否是主键索引
     */
    private Boolean primaryKey;
    
    /**
     * 索引的注释或描述
     */
    private String comment;
    
    /**
     * 索引的筛选条件（部分索引）
     */
    private String filterCondition;
    
    /**
     * 索引中的列信息列表
     */
    private List<IndexColumnInfo> columns;
    
    /**
     * 添加索引列信息
     * 
     * @param column 索引列信息
     */
    public void addColumn(IndexColumnInfo column) {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        if (column != null) {
            columns.add(column);
        }
    }
    
    /**
     * 获取索引列名列表
     * 
     * @return 索引列名列表
     */
    public List<String> getColumnNames() {
        if (columns == null) {
            return Collections.emptyList();
        }
        
        return columns.stream()
                .map(IndexColumnInfo::getColumnName)
                .collect(Collectors.toList());
    }
    
    /**
     * 判断是否是唯一索引
     * 
     * @return 如果是唯一索引返回true，否则返回false
     */
    public boolean isUnique() {
        return unique != null && unique;
    }
    
    /**
     * 判断是否是主键索引
     * 
     * @return 如果是主键索引返回true，否则返回false
     */
    public boolean isPrimaryKey() {
        return primaryKey != null && primaryKey;
    }
    
    /**
     * 是否是部分索引
     * 
     * @return 如果是部分索引返回true，否则返回false
     */
    public boolean isPartial() {
        return filterCondition != null && !filterCondition.isEmpty();
    }
    
    /**
     * 获取完整的索引标识
     * 
     * @return 格式为"schema.table.index"的完整索引标识
     */
    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if (schemaName != null && !schemaName.isEmpty()) {
            sb.append(schemaName).append(".");
        }
        if (tableName != null && !tableName.isEmpty()) {
            sb.append(tableName).append(".");
        }
        if (name != null) {
            sb.append(name);
        }
        return sb.toString();
    }
    
    /**
     * 获取索引定义的DDL语句
     * 
     * @return 表示索引定义的DDL语句
     */
    public String getDDLStatement() {
        if (columns == null || columns.isEmpty()) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE ");
        
        if (isUnique()) {
            sb.append("UNIQUE ");
        }
        
        sb.append("INDEX ");
        sb.append(name);
        sb.append(" ON ");
        
        if (schemaName != null && !schemaName.isEmpty()) {
            sb.append(schemaName).append(".");
        }
        
        sb.append(tableName);
        sb.append(" (");
        
        // 添加索引列定义
        List<String> columnDefinitions = new ArrayList<>();
        for (IndexColumnInfo column : columns) {
            StringBuilder colDef = new StringBuilder();
            if (column.isExpression()) {
                colDef.append(column.getExpression());
            } else {
                colDef.append(column.getColumnName());
            }
            
            // 添加排序方向
            if (!column.isAscending()) {
                colDef.append(" DESC");
            }
            
            columnDefinitions.add(colDef.toString());
        }
        
        sb.append(String.join(", ", columnDefinitions));
        sb.append(")");
        
        // 添加过滤条件（如果有）
        if (isPartial()) {
            sb.append(" WHERE ").append(filterCondition);
        }
        
        return sb.toString();
    }
}