package com.insightdata.domain.model.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 索引列信息模型
 * 用于表示索引中列的详细信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexColumnInfo {

    private Long id;
    
    /**
     * 所属索引名称
     */
    private String indexName;
    
    /**
     * 列名
     */
    private String columnName;
    
    /**
     * 序号位置
     */
    private Integer ordinalPosition;
    
    /**
     * 排序方向（ASC, DESC）
     */
    private String sortOrder;
    
    /**
     * 排序规则
     */
    private String collation;
    
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
     * 表达式（对于函数索引）
     */
    private String expression;
    
    /**
     * 判断是否是表达式索引
     * 
     * @return 如果是表达式索引返回true，否则返回false
     */
    public boolean isExpression() {
        return expression != null && !expression.isEmpty();
    }
    
    /**
     * 判断是否是按升序排序
     * 
     * @return 如果是升序返回true，否则返回false
     */
    public boolean isAscending() {
        if (sortOrder == null) {
            // 默认为升序
            return true;
        }
        return "A".equalsIgnoreCase(sortOrder) || 
               "ASC".equalsIgnoreCase(sortOrder) || 
               "ASCENDING".equalsIgnoreCase(sortOrder);
    }
    
    /**
     * 获取完整的列标识
     * 
     * @return 格式为"schema.table.column"的完整标识，或表达式
     */
    public String getFullColumnIdentifier() {
        if (isExpression()) {
            return expression;
        }
        
        StringBuilder sb = new StringBuilder();
        if (schemaName != null && !schemaName.isEmpty()) {
            sb.append(schemaName).append(".");
        }
        if (tableName != null && !tableName.isEmpty()) {
            sb.append(tableName).append(".");
        }
        if (columnName != null) {
            sb.append(columnName);
        }
        return sb.toString();
    }
}