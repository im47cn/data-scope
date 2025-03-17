package com.domain.model.metadata;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 列信息实体类
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnInfo {

    /**
     * 列ID，使用UUID
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
     * 列名
     */
    private String name;
    
    /**
     * 列位置顺序
     */
    private Integer position;
    
    /**
     * 列数据类型
     */
    private String dataType;
    
    /**
     * 原始数据类型（数据库原始类型）
     */
    private String nativeDataType;
    
    /**
     * 列长度
     */
    private Integer length;
    
    /**
     * 数字精度
     */
    private Integer precision;
    
    /**
     * 小数位数
     */
    private Integer scale;
    
    /**
     * 是否主键
     */
    private Boolean primaryKey;
    
    /**
     * 是否唯一键
     */
    private Boolean unique;
    
    /**
     * 是否可空
     */
    private Boolean nullable;
    
    /**
     * 是否自增
     */
    private Boolean autoIncrement;
    
    /**
     * 是否虚拟列
     */
    private Boolean virtual;
    
    /**
     * 是否隐藏列
     */
    private Boolean hidden;
    
    /**
     * 默认值
     */
    private String defaultValue;
    
    /**
     * 列描述
     */
    private String description;
    
    /**
     * 列注释
     */
    private String comment;
    
    /**
     * 生成表达式（如果是计算列）
     */
    private String generationExpression;
    
    /**
     * 字符集（如果适用）
     */
    private String characterSet;
    
    /**
     * 排序规则（如果适用）
     */
    private String collation;
    
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
     * 判断列是否为主键
     * 
     * @return 是否主键
     */
    public boolean isPrimaryKey() {
        return Boolean.TRUE.equals(primaryKey);
    }
    
    /**
     * 判断列是否为唯一键
     * 
     * @return 是否唯一键
     */
    public boolean isUnique() {
        return Boolean.TRUE.equals(unique);
    }
    
    /**
     * 判断列是否可为空
     * 
     * @return 是否可为空
     */
    public boolean isNullable() {
        return Boolean.TRUE.equals(nullable);
    }
    
    /**
     * 判断列是否为自增列
     * 
     * @return 是否自增
     */
    public boolean isAutoIncrement() {
        return Boolean.TRUE.equals(autoIncrement);
    }
    
    /**
     * 判断列是否为数字类型
     * 
     * @return 是否数字类型
     */
    public boolean isNumeric() {
        if (dataType == null) {
            return false;
        }
        
        String type = dataType.toLowerCase();
        return type.contains("int") || 
               type.contains("decimal") || 
               type.contains("numeric") ||
               type.contains("float") || 
               type.contains("double") || 
               type.contains("real") ||
               type.contains("bit") || 
               type.contains("number");
    }
    
    /**
     * 判断列是否为日期类型
     * 
     * @return 是否日期类型
     */
    public boolean isDateType() {
        if (dataType == null) {
            return false;
        }
        
        String type = dataType.toLowerCase();
        return type.contains("date") || 
               type.contains("time") || 
               type.contains("timestamp");
    }
    
    /**
     * 判断列是否为字符串类型
     *
     * @return 是否字符串类型
     */
    public boolean isStringType() {
        if (dataType == null) {
            return false;
        }
        
        String type = dataType.toLowerCase();
        return type.contains("char") ||
               type.contains("text") ||
               type.contains("json") ||
               type.contains("xml") ||
               type.contains("enum") ||
               type.contains("set");
    }
    
    /**
     * 兼容方法：判断列是否为日期时间类型
     *
     * @return 是否日期时间类型
     */
    public boolean isDateTime() {
        return isDateType();
    }
    
    /**
     * 兼容方法：判断列是否为字符串类型
     *
     * @return 是否字符串类型
     */
    public boolean isString() {
        return isStringType();
    }
}