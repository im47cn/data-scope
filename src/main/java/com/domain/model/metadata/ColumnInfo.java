package com.domain.model.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 列信息实体类
 * 表示数据库表的列及其元数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ColumnInfo {

    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 模式名称
     */
    private String schemaName;
    
    /**
     * 表名称
     */
    private String tableName;
    
    /**
     * 列名称
     */
    private String name;
    
    /**
     * 列序号（在表中的位置）
     */
    private Integer ordinalPosition;
    
    /**
     * 默认值
     */
    private String defaultValue;
    
    /**
     * 是否可为空
     */
    private boolean nullable;
    
    /**
     * 数据类型
     */
    private String dataType;
    
    /**
     * 字符最大长度
     */
    private Integer length;
    
    /**
     * 数值精度
     */
    private Integer numericPrecision;
    
    /**
     * 数值刻度
     */
    private Integer numericScale;
    
    /**
     * 日期时间精度
     */
    private Integer datetimePrecision;
    
    /**
     * 字符集名称
     */
    private String characterSetName;
    
    /**
     * 排序规则名称
     */
    private String collationName;
    
    /**
     * 列类型（数据库原始类型）
     */
    private String columnType;
    
    /**
     * 是否为主键
     */
    private boolean primaryKey;
    
    /**
     * 是否为外键
     */
    private boolean foreignKey;
    
    /**
     * 是否唯一
     */
    private boolean unique;
    
    /**
     * 是否自增
     */
    private boolean autoIncrement;
    
    /**
     * 是否为生成的列
     */
    private boolean generated;
    
    /**
     * 生成表达式
     */
    private String generationExpression;
    
    /**
     * 列的描述/注释
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

    private String remarks;
    
    /**
     * 检查是否为数值类型列
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
               type.contains("numeric") || 
               type.contains("real") || 
               type.contains("money") || 
               type.contains("number") || 
               type.contains("bit");
    }
    
    /**
     * 检查是否为字符串类型列
     */
    public boolean isString() {
        if (dataType == null) {
            return false;
        }
        
        String type = dataType.toLowerCase();
        return type.contains("char") || 
               type.contains("text") || 
               type.contains("string") || 
               type.contains("varchar") || 
               type.contains("clob");
    }
    
    /**
     * 检查是否为日期时间类型列
     */
    public boolean isDateTime() {
        if (dataType == null) {
            return false;
        }
        
        String type = dataType.toLowerCase();
        return type.contains("date") || 
               type.contains("time") || 
               type.contains("timestamp") || 
               type.contains("interval");
    }
    
    /**
     * 检查是否为BLOB/二进制类型列
     */
    public boolean isBinary() {
        if (dataType == null) {
            return false;
        }
        
        String type = dataType.toLowerCase();
        return type.contains("blob") || 
               type.contains("binary") || 
               type.contains("image") || 
               type.contains("raw");
    }
    
    /**
     * 检查是否为布尔类型列
     */
    public boolean isBoolean() {
        if (dataType == null) {
            return false;
        }
        
        String type = dataType.toLowerCase();
        return type.contains("bool") || 
               type.equals("bit") || 
               type.contains("logical");
    }
    
    /**
     * 检查是否为枚举类型列
     */
    public boolean isEnum() {
        if (dataType == null) {
            return false;
        }
        
        String type = dataType.toLowerCase();
        return type.contains("enum");
    }
    
    /**
     * 检查是否为JSON类型列
     */
    public boolean isJson() {
        if (dataType == null) {
            return false;
        }
        
        String type = dataType.toLowerCase();
        return type.contains("json");
    }
    
    /**
     * 检查是否为XML类型列
     */
    public boolean isXml() {
        if (dataType == null) {
            return false;
        }
        
        String type = dataType.toLowerCase();
        return type.contains("xml");
    }
    
    /**
     * 检查是否为几何/地理类型列
     */
    public boolean isGeospatial() {
        if (dataType == null) {
            return false;
        }
        
        String type = dataType.toLowerCase();
        return type.contains("geo") || 
               type.contains("geometry") || 
               type.contains("point") || 
               type.contains("polygon") || 
               type.contains("linestring");
    }
    
    /**
     * 获取列的展示类型（更友好的类型名称）
     */
    public String getDisplayType() {
        if (isNumeric()) {
            if (dataType.toLowerCase().contains("int")) {
                return "整数";
            } else {
                return "数值";
            }
        } else if (isString()) {
            return "文本";
        } else if (isDateTime()) {
            if (dataType.toLowerCase().contains("date") && !dataType.toLowerCase().contains("time")) {
                return "日期";
            } else if (dataType.toLowerCase().contains("time") && !dataType.toLowerCase().contains("date")) {
                return "时间";
            } else {
                return "日期时间";
            }
        } else if (isBoolean()) {
            return "布尔值";
        } else if (isBinary()) {
            return "二进制";
        } else if (isJson()) {
            return "JSON";
        } else if (isXml()) {
            return "XML";
        } else if (isGeospatial()) {
            return "地理数据";
        } else if (isEnum()) {
            return "枚举";
        } else {
            return dataType;
        }
    }
    
    /**
     * 获取列的完全限定名
     */
    public String getQualifiedName() {
        if (tableName != null && !tableName.isEmpty()) {
            if (schemaName != null && !schemaName.isEmpty()) {
                return schemaName + "." + tableName + "." + name;
            }
            return tableName + "." + name;
        }
        return name;
    }
    
    /**
     * 获取列的显示信息（带备注）
     */
    public String getDisplayInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        
        if (description != null && !description.isEmpty()) {
            sb.append(" (").append(description).append(")");
        }
        
        sb.append(": ").append(getDisplayType());
        
        if (primaryKey) {
            sb.append(" [主键]");
        }
        
        if (foreignKey) {
            sb.append(" [外键]");
        }
        
        return sb.toString();
    }
}