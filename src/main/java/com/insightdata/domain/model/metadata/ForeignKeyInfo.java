package com.insightdata.domain.model.metadata;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 外键信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForeignKeyInfo {
    /**
     * 外键ID
     */
    private Long id;
    
    /**
     * 外键名称
     */
    private String name;
    
    /**
     * 源表ID
     */
    private Long sourceTableId;
    
    /**
     * 目标表ID
     */
    private Long targetTableId;
    
    /**
     * 更新规则（CASCADE, RESTRICT, SET NULL等）
     */
    private String updateRule;
    
    /**
     * 删除规则（CASCADE, RESTRICT, SET NULL等）
     */
    private String deleteRule;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 源表信息（非持久化字段）
     */
    private TableInfo sourceTable;
    
    /**
     * 目标表信息（非持久化字段）
     */
    private TableInfo targetTable;
    
    /**
     * 外键列映射列表
     */
    @Builder.Default
    private List<ForeignKeyColumnInfo> columns = new ArrayList<>();
}