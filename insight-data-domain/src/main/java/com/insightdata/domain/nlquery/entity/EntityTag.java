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