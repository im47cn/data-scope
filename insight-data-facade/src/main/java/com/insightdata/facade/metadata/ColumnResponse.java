package com.insightdata.facade.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 数据列响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnResponse {
    
    /**
     * ID
     */
    private String id;
    
    /**
     * 表ID
     */
    private Long tableId;
    
    /**
     * 列名
     */
    private String name;
    
    /**
     * 数据类型
     */
    private String dataType;
    
    /**
     * 类型名称
     */
    private String typeName;
    
    /**
     * 列大小
     */
    private Integer columnSize;
    
    /**
     * 小数位数
     */
    private Integer decimalDigits;
    
    /**
     * 是否可空
     */
    private boolean nullable;
    
    /**
     * 默认值
     */
    private String defaultValue;
    
    /**
     * 是否主键
     */
    private boolean primaryKey;
    
    /**
     * 是否唯一键
     */
    private boolean uniqueKey;
    
    /**
     * 是否外键
     */
    private boolean foreignKey;
    
    /**
     * 是否自增
     */
    private boolean autoIncrement;
    
    /**
     * 序号
     */
    private Integer ordinalPosition;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 字符集
     */
    private String characterSet;
    
    /**
     * 排序规则
     */
    private String collation;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 是否数字类型
     */
    public boolean isNumeric() {
        if (dataType == null) {
            return false;
        }
        String type = dataType.toLowerCase();
        return type.contains("int") || 
               type.contains("float") || 
               type.contains("double") || 
               type.contains("decimal") || 
               type.contains("numeric");
    }
    
    /**
     * 是否字符串类型
     */
    public boolean isString() {
        if (dataType == null) {
            return false;
        }
        String type = dataType.toLowerCase();
        return type.contains("char") || 
               type.contains("text") || 
               type.contains("json") || 
               type.contains("xml");
    }
    
    /**
     * 是否日期时间类型
     */
    public boolean isDateTime() {
        if (dataType == null) {
            return false;
        }
        String type = dataType.toLowerCase();
        return type.contains("date") || 
               type.contains("time") || 
               type.contains("timestamp");
    }
    
    /**
     * 是否布尔类型
     */
    public boolean isBoolean() {
        if (dataType == null) {
            return false;
        }
        String type = dataType.toLowerCase();
        return type.contains("bool") || 
               type.contains("bit");
    }
    
    /**
     * 是否二进制类型
     */
    public boolean isBinary() {
        if (dataType == null) {
            return false;
        }
        String type = dataType.toLowerCase();
        return type.contains("blob") || 
               type.contains("binary") || 
               type.contains("varbinary");
    }
}