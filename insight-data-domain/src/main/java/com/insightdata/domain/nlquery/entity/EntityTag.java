package com.insightdata.domain.nlquery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity tag class that represents an identified entity in text
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityTag {

    /**
     * Entity type
     */
    private EntityType type;

    /**
     * Entity value
     */
    private String value;

    /**
     * Start offset in text
     */
    private int startOffset;

    /**
     * End offset in text
     */
    private int endOffset;

    /**
     * Confidence score
     */
    private double confidence;

    /**
     * Entity source
     */
    private EntitySource source;

    /**
     * Additional metadata
     */
    private String metadata;

    /**
     * Entity type enum
     */
    public enum EntityType {
        TABLE,          // 表名
        COLUMN,         // 列名
        VALUE,          // 值
        FUNCTION,       // 函数
        OPERATOR,       // 运算符
        CONDITION,      // 条件
        ORDER,          // 排序
        LIMIT,          // 限制
        GROUP,          // 分组
        DATETIME,       // 日期时间
        NUMBER,         // 数字
        STRING,         // 字符串
        BOOLEAN,        // 布尔值
        UNKNOWN         // 未知类型
    }

    /**
     * Entity source enum
     */
    public enum EntitySource {
        METADATA,           // 元数据
        RULE,              // 规则
        DICTIONARY,        // 字典
        MACHINE_LEARNING,  // 机器学习
        USER_FEEDBACK,     // 用户反馈
        UNKNOWN           // 未知来源
    }
}