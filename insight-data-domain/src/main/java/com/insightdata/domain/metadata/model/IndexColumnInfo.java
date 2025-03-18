package com.insightdata.domain.metadata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 索引列信息实体类
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class IndexColumnInfo {
    
    /**
     * 索引列ID，使用UUID
     */
    private String id;
    
    /**
     * 索引ID
     */
    private String indexId;
    
    /**
     * 列名
     */
    private String columnName;
    
    /**
     * 位置（从1开始）
     */
    private int position;
    
    /**
     * 是否升序排序
     */
    @Builder.Default
    private boolean ascending = true;
    
    /**
     * 排序方向
     */
    private String sortOrder;
    
    /**
     * 列的表达式（用于函数索引）
     */
    private String expression;
    
    /**
     * 操作符类（用于某些特殊索引类型）
     */
    private String operatorClass;
    
    /**
     * 排序规则
     */
    private String collation;
    
    /**
     * 列统计信息
     */
    private String statistics;
    
    /**
     * 设置为降序排序
     */
    public void setDescending() {
        this.ascending = false;
        this.sortOrder = "DESC";
    }
    
    /**
     * 设置为升序排序
     */
    public void setAscending() {
        this.ascending = true;
        this.sortOrder = "ASC";
    }
    
    /**
     * 获取排序方向描述
     * 
     * @return 排序方向描述
     */
    public String getSortDirection() {
        return ascending ? "升序" : "降序";
    }
    
    /**
     * 判断是否是表达式列
     * 
     * @return 是否是表达式列
     */
    public boolean isExpression() {
        return expression != null && !expression.isEmpty();
    }
    
    /**
     * 获取列描述
     * 
     * @return 列描述
     */
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(columnName);
        
        if (!ascending) {
            sb.append(" 降序");
        }
        
        if (isExpression()) {
            sb.append(" (").append(expression).append(")");
        }
        
        if (operatorClass != null && !operatorClass.isEmpty()) {
            sb.append(" using ").append(operatorClass);
        }
        
        return sb.toString();
    }
}