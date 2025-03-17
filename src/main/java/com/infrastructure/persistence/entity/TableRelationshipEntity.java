package com.infrastructure.persistence.entity;

import com.domain.model.metadata.TableRelationship.RelationshipSource;
import com.domain.model.metadata.TableRelationship.RelationshipType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 表关系实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableRelationshipEntity {
    
    /**
     * 关系ID
     */
    private Long id;
    
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 源表名
     */
    private String sourceTable;
    
    /**
     * 源列名
     */
    private String sourceColumn;
    
    /**
     * 目标表名
     */
    private String targetTable;
    
    /**
     * 目标列名
     */
    private String targetColumn;
    
    /**
     * 关系类型
     */
    private RelationshipType relationshipType;
    
    /**
     * 关系来源
     */
    private RelationshipSource relationshipSource;
    
    /**
     * 关系权重（置信度）
     * 范围：0.0-1.0
     */
    private double weight;
    
    /**
     * 使用频率
     */
    private int frequency;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}