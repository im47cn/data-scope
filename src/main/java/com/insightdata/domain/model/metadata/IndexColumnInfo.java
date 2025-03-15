package com.insightdata.domain.model.metadata;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 索引列信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexColumnInfo {
    /**
     * ID
     */
    private Long id;
    
    /**
     * 索引ID
     */
    private Long indexId;
    
    /**
     * 列ID
     */
    private Long columnId;
    
    /**
     * 列在索引中的位置
     */
    private Integer ordinalPosition;
    
    /**
     * 排序方式（ASC, DESC）
     */
    private String sortOrder;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 列信息（非持久化字段）
     */
    private ColumnInfo column;
}