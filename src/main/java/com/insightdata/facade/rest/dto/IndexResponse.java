package com.insightdata.facade.rest.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 索引响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexResponse {
    
    /**
     * ID
     */
    private Long id;
    
    /**
     * 表ID
     */
    private Long tableId;
    
    /**
     * 索引名称
     */
    private String name;
    
    /**
     * 索引类型
     */
    private String type;
    
    /**
     * 是否唯一
     */
    private boolean unique;
    
    /**
     * 是否主键
     */
    private boolean primaryKey;
    
    /**
     * 是否聚簇索引
     */
    private boolean clustered;
    
    /**
     * 索引方法
     */
    private String method;
    
    /**
     * 索引列列表
     */
    @Builder.Default
    private List<IndexColumnResponse> columns = new ArrayList<>();
    
    /**
     * 过滤条件
     */
    private String filterCondition;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 添加索引列
     */
    public IndexResponse addColumn(IndexColumnResponse column) {
        columns.add(column);
        return this;
    }
    
    /**
     * 获取索引列名列表
     */
    public List<String> getColumnNames() {
        List<String> names = new ArrayList<>();
        for (IndexColumnResponse column : columns) {
            names.add(column.getColumnName());
        }
        return names;
    }
    
    /**
     * 获取索引列表达式列表
     */
    public List<String> getColumnExpressions() {
        List<String> expressions = new ArrayList<>();
        for (IndexColumnResponse column : columns) {
            if (column.isExpression()) {
                expressions.add(column.getExpression());
            } else {
                expressions.add(column.getColumnName());
            }
        }
        return expressions;
    }
    
    /**
     * 获取完整的索引定义
     */
    public String getIndexDefinition() {
        StringBuilder sb = new StringBuilder();
        
        // 添加索引类型
        if (primaryKey) {
            sb.append("PRIMARY KEY ");
        } else if (unique) {
            sb.append("UNIQUE ");
        }
        
        // 添加索引方法
        if (method != null && !method.isEmpty()) {
            sb.append("USING ").append(method).append(" ");
        }
        
        // 添加索引名称
        sb.append("INDEX ").append(name).append(" ");
        
        // 添加索引列
        sb.append("(");
        List<String> columnDefs = new ArrayList<>();
        for (IndexColumnResponse column : columns) {
            StringBuilder colDef = new StringBuilder();
            if (column.isExpression()) {
                colDef.append(column.getExpression());
            } else {
                colDef.append(column.getColumnName());
            }
            if (!column.isAscending()) {
                colDef.append(" DESC");
            }
            columnDefs.add(colDef.toString());
        }
        sb.append(String.join(", ", columnDefs));
        sb.append(")");
        
        // 添加过滤条件
        if (filterCondition != null && !filterCondition.isEmpty()) {
            sb.append(" WHERE ").append(filterCondition);
        }
        
        return sb.toString();
    }
}