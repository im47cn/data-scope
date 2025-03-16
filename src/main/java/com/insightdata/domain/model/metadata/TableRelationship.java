package com.insightdata.domain.model.metadata;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表关系模型
 * 表示两个表之间的关系，包括关系类型、来源和权重信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableRelationship {
    
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
    private RelationshipType type;
    
    /**
     * 关系来源
     */
    private RelationshipSource source;
    
    /**
     * 关系权重（置信度）
     * 范围：0.0-1.0
     */
    private double weight;
    
    /**
     * 使用频率
     * 表示该关系在查询中被使用的次数
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
    
    /**
     * 关系类型枚举
     */
    public enum RelationshipType {
        /**
         * 一对一关系
         */
        ONE_TO_ONE,
        
        /**
         * 一对多关系
         */
        ONE_TO_MANY,
        
        /**
         * 多对一关系
         */
        MANY_TO_ONE,
        
        /**
         * 多对多关系
         */
        MANY_TO_MANY
    }
    
    /**
     * 关系来源枚举
     */
    public enum RelationshipSource {
        /**
         * 从数据库元数据中提取（如外键约束）
         */
        METADATA,
        
        /**
         * 通过推理算法推断（如命名规则、数据模式）
         */
        INFERENCE,
        
        /**
         * 用户反馈
         */
        USER_FEEDBACK,
        
        /**
         * 从查询历史学习
         */
        LEARNING
    }
}