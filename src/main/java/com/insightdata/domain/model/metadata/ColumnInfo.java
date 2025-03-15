package com.insightdata.domain.model.metadata;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库列信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnInfo {
    /**
     * 列ID
     */
    private Long id;
    
    /**
     * 表ID
     */
    private Long tableId;
    
    /**
     * 列名称
     */
    private String name;
    
    /**
     * 数据类型（VARCHAR, INTEGER等）
     */
    private String dataType;
    
    /**
     * 列类型（包含长度、精度等信息，如VARCHAR(255)）
     */
    private String columnType;
    
    /**
     * 列在表中的位置
     */
    private Integer ordinalPosition;
    
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
     * 描述
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
     * 是否为自增列
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