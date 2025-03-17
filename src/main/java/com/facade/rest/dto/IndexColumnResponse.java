package com.facade.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 索引列响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexColumnResponse {
    
    /**
     * ID
     */
    private String id;
    
    /**
     * 索引ID
     */
    private Long indexId;
    
    /**
     * 列名
     */
    private String columnName;
    
    /**
     * 序号
     */
    private Integer ordinalPosition;
    
    /**
     * 是否升序
     */
    private boolean ascending;
    
    /**
     * 是否表达式
     */
    private boolean expression;
    
    /**
     * 表达式内容
     */
    private String expressionContent;
    
    /**
     * 排序规则
     */
    private String collation;
    
    /**
     * 操作符类
     */
    private String operatorClass;
    
    /**
     * 是否包含在索引中
     */
    private boolean included;
    
    /**
     * 获取表达式
     */
    public String getExpression() {
        return expression ? expressionContent : columnName;
    }
    
    /**
     * 获取完整的列定义
     */
    public String getColumnDefinition() {
        StringBuilder sb = new StringBuilder();
        
        // 添加列名或表达式
        if (expression) {
            sb.append(expressionContent);
        } else {
            sb.append(columnName);
        }
        
        // 添加排序方向
        if (!ascending) {
            sb.append(" DESC");
        }
        
        // 添加排序规则
        if (collation != null && !collation.isEmpty()) {
            sb.append(" COLLATE ").append(collation);
        }
        
        // 添加操作符类
        if (operatorClass != null && !operatorClass.isEmpty()) {
            sb.append(" ").append(operatorClass);
        }
        
        return sb.toString();
    }
}