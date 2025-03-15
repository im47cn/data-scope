package com.insightdata.domain.model.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库表信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableInfo {
    /**
     * 表ID
     */
    private Long id;
    
    /**
     * 模式ID
     */
    private Long schemaId;
    
    /**
     * 表名称
     */
    private String name;
    
    /**
     * 表类型（TABLE, VIEW, SYSTEM_TABLE等）
     */
    private String type;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 估计行数
     */
    private Long estimatedRowCount;
    
    /**
     * 数据大小（字节）
     */
    private Long dataSize;
    
    /**
     * 索引大小（字节）
     */
    private Long indexSize;
    
    /**
     * 最后分析时间
     */
    private LocalDateTime lastAnalyzed;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 列列表
     */
    @Builder.Default
    private List<ColumnInfo> columns = new ArrayList<>();
    
    /**
     * 索引列表
     */
    @Builder.Default
    private List<IndexInfo> indexes = new ArrayList<>();
    
    /**
     * 外键列表
     */
    @Builder.Default
    private List<ForeignKeyInfo> foreignKeys = new ArrayList<>();
}