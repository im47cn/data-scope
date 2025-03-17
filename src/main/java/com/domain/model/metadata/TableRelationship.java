package com.domain.model.metadata;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 表关系实体类
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TableRelationship {

    /**
     * 主键ID
     */
    private String id;
    
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 源表名
     */
    private String sourceTableName;
    
    /**
     * 源列名
     */
    private String sourceColumnNames;
    
    /**
     * 目标表名
     */
    private String targetTableName;
    
    /**
     * 目标列名
     */
    private String targetColumnNames;
    
    /**
     * 关系类型
     */
    private RelationshipType relationType;
    
    /**
     * 关系来源
     */
    private RelationshipSource relationSource;
    
    /**
     * 关系置信度（0-1）
     */
    private Double confidence;
    
    /**
     * 使用频率
     */
    private Integer frequency;
    
    /**
     * 乐观锁版本号
     */
    private Long nonce;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 创建者
     */
    private String createdBy;
    
    /**
     * 更新者
     */
    private String updatedBy;
    
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
        MANY_TO_MANY,
        
        /**
         * 自连接关系
         */
        SELF_REFERENCE
    }
    
    /**
     * 关系来源枚举
     */
    public enum RelationshipSource {
        /**
         * 外键约束
         */
        FOREIGN_KEY,
        
        /**
         * 列名匹配
         */
        COLUMN_NAME_MATCH,
        
        /**
         * 查询模式学习
         */
        QUERY_PATTERN,
        
        /**
         * 用户反馈
         */
        USER_FEEDBACK,
        
        /**
         * AI推断
         */
        AI_INFERENCE
    }
}