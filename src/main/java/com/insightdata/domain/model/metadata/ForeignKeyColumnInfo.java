package com.insightdata.domain.model.metadata;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 外键列信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForeignKeyColumnInfo {
    /**
     * ID
     */
    private Long id;
    
    /**
     * 外键ID
     */
    private Long foreignKeyId;
    
    /**
     * 源列ID
     */
    private Long sourceColumnId;
    
    /**
     * 目标列ID
     */
    private Long targetColumnId;
    
    /**
     * 列在外键中的位置
     */
    private Integer ordinalPosition;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 源列信息（非持久化字段）
     */
    private ColumnInfo sourceColumn;
    
    /**
     * 目标列信息（非持久化字段）
     */
    private ColumnInfo targetColumn;
}