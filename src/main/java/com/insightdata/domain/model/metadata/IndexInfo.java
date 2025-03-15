package com.insightdata.domain.model.metadata;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库索引信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexInfo {
    /**
     * 索引ID
     */
    private Long id;
    
    /**
     * 表ID
     */
    private Long tableId;
    
    /**
     * 索引名称
     */
    private String name;
    
    /**
     * 索引类型（BTREE, HASH等）
     */
    private String type;
    
    /**
     * 是否唯一索引
     */
    private Boolean isUnique;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 索引列列表
     */
    @Builder.Default
    private List<IndexColumnInfo> columns = new ArrayList<>();
}