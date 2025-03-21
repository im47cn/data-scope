package com.insightdata.domain.metadata.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 列信息
 * 包含数据库表列的基本信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnInfo {
    
    /**
     * 列ID
     */
    private String id;
    
    /**
     * 表ID
     */
    private String tableId;
    
    /**
     * 列名
     */
    private String name;
    
    /**
     * 列序号
     */
    private Integer ordinalPosition;
    
    /**
     * 列默认值
     */
    private String defaultValue;
    
    /**
     * 是否可为空
     */
    private Boolean nullable;
    
    /**
     * 数据类型
     */
    private String dataType;
    
    /**
     * 字符最大长度
     */
    private Integer characterMaximumLength;
    
    /**
     * 数字精度
     */
    private Integer numericPrecision;
    
    /**
     * 数字刻度
     */
    private Integer numericScale;
    
    /**
     * 日期时间精度
     */
    private Integer datetimePrecision;
    
    /**
     * 列描述
     */
    private String description;
    
    /**
     * 是否为主键
     */
    private Boolean isPrimaryKey;
    
    /**
     * 是否为外键
     */
    private Boolean isForeignKey;
    
    /**
     * 是否为唯一键
     */
    private Boolean isUniqueKey;
    
    /**
     * 是否为索引
     */
    private Boolean isIndexed;
    
    /**
     * 是否自增
     */
    private Boolean isAutoIncrement;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 表名（非数据库字段，用于展示）
     */
    private String tableName;
    
    /**
     * 获取完整的列名（表名.列名）
     * @return 完整列名
     */
    public String getFullName() {
        if (tableName != null && !tableName.isEmpty()) {
            return tableName + "." + name;
        }
        return name;
    }
}