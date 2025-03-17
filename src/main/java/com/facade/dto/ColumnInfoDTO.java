package com.facade.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库列信息DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnInfoDTO {
    
    /**
     * 列ID
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
     * 列序号（从1开始）
     */
    private Integer ordinalPosition;
    
    /**
     * 数据类型
     */
    private String dataType;
    
    /**
     * 原始数据类型（数据库原始类型）
     */
    private String nativeDataType;
    
    /**
     * 长度
     */
    private Integer length;
    
    /**
     * 精度
     */
    private Integer precision;
    
    /**
     * 小数位数
     */
    private Integer scale;
    
    /**
     * 是否可为空
     */
    private Boolean nullable;
    
    /**
     * 默认值
     */
    private String defaultValue;
    
    /**
     * 描述信息
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
    private Boolean isUnique;
    
    /**
     * 是否为索引
     */
    private Boolean isIndexed;
    
    /**
     * 是否为自增
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
}