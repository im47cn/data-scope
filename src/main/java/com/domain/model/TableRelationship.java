package com.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 表关系
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TableRelationship {

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
     * 权重
     */
    private double weight;

    /**
     * 出现频率
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
         * 外键关系
         */
        FOREIGN_KEY,

        /**
         * 相同名称关系
         */
        SAME_NAME,

        /**
         * 相同类型关系
         */
        SAME_TYPE,

        /**
         * 查询关联关系
         */
        QUERY_JOIN,

        /**
         * 用户反馈关系
         */
        USER_FEEDBACK
    }

    /**
     * 关系来源枚举
     */
    public enum RelationshipSource {
        /**
         * 元数据分析
         */
        METADATA_ANALYSIS,

        /**
         * 查询历史分析
         */
        QUERY_HISTORY,

        /**
         * 用户反馈
         */
        USER_FEEDBACK,

        /**
         * 机器学习
         */
        MACHINE_LEARNING
    }
}