package com.facade.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 外键列响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForeignKeyColumnResponse {
    
    /**
     * ID
     */
    private Long id;
    
    /**
     * 外键ID
     */
    private Long foreignKeyId;
    
    /**
     * 源列名
     */
    private String sourceColumnName;
    
    /**
     * 目标列名
     */
    private String targetColumnName;
    
    /**
     * 序号
     */
    private Integer ordinalPosition;
    
    /**
     * 源列数据类型
     */
    private String sourceDataType;
    
    /**
     * 目标列数据类型
     */
    private String targetDataType;
    
    /**
     * 源列是否可空
     */
    private boolean sourceNullable;
    
    /**
     * 目标列是否可空
     */
    private boolean targetNullable;
    
    /**
     * 源列默认值
     */
    private String sourceDefaultValue;
    
    /**
     * 目标列默认值
     */
    private String targetDefaultValue;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 获取完整的列定义
     */
    public String getColumnDefinition() {
        StringBuilder sb = new StringBuilder();
        
        // 添加源列名和目标列名
        sb.append(sourceColumnName).append(" -> ").append(targetColumnName);
        
        // 添加数据类型
        if (sourceDataType != null && targetDataType != null) {
            sb.append(" (").append(sourceDataType);
            if (!sourceDataType.equals(targetDataType)) {
                sb.append(" -> ").append(targetDataType);
            }
            sb.append(")");
        }
        
        // 添加可空性
        if (sourceNullable != targetNullable) {
            sb.append(" [");
            sb.append(sourceNullable ? "NULL" : "NOT NULL");
            sb.append(" -> ");
            sb.append(targetNullable ? "NULL" : "NOT NULL");
            sb.append("]");
        }
        
        return sb.toString();
    }
}