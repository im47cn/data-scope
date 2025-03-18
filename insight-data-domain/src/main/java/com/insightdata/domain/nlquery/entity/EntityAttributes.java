package com.insightdata.domain.nlquery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 实体属性类
 * 用于存储实体的额外属性信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityAttributes {

    /**
     * 原始文本
     */
    private String originalText;

    /**
     * 规范化后的文本
     */
    private String normalizedText;

    /**
     * 开始位置
     */
    private int startOffset;

    /**
     * 结束位置
     */
    private int endOffset;

    /**
     * 在文本中的顺序
     */
    private int position;

    /**
     * 词性标注
     */
    private String posTag;

    /**
     * 依存关系
     */
    private String dependencyLabel;

    /**
     * 语义角色
     */
    private String semanticRole;

    /**
     * 置信度
     */
    private double confidence;

    /**
     * 其他属性
     */
    @Builder.Default
    private Map<String, Object> properties = new HashMap<>();

    /**
     * 创建基础属性
     *
     * @param text 原始文本
     * @param start 开始位置
     * @param end 结束位置
     * @return 实体属性
     */
    public static EntityAttributes basic(String text, int start, int end) {
        return builder()
                .originalText(text)
                .startOffset(start)
                .endOffset(end)
                .confidence(1.0)
                .build();
    }

    /**
     * 创建带有规范化文本的属性
     *
     * @param text 原始文本
     * @param normalizedText 规范化文本
     * @param start 开始位置
     * @param end 结束位置
     * @return 实体属性
     */
    public static EntityAttributes normalized(String text, String normalizedText, int start, int end) {
        return builder()
                .originalText(text)
                .normalizedText(normalizedText)
                .startOffset(start)
                .endOffset(end)
                .confidence(1.0)
                .build();
    }

    /**
     * 创建带有位置信息的属性
     *
     * @param text 原始文本
     * @param start 开始位置
     * @param end 结束位置
     * @param position 在文本中的位置
     * @return 实体属性
     */
    public static EntityAttributes withPosition(String text, int start, int end, int position) {
        return builder()
                .originalText(text)
                .startOffset(start)
                .endOffset(end)
                .position(position)
                .confidence(1.0)
                .build();
    }

    /**
     * 创建带有语言特征的属性
     *
     * @param text 原始文本
     * @param posTag 词性标注
     * @param dependencyLabel 依存关系
     * @param semanticRole 语义角色
     * @return 实体属性
     */
    public static EntityAttributes withLinguisticFeatures(String text, String posTag, 
            String dependencyLabel, String semanticRole) {
        return builder()
                .originalText(text)
                .posTag(posTag)
                .dependencyLabel(dependencyLabel)
                .semanticRole(semanticRole)
                .confidence(1.0)
                .build();
    }

    /**
     * 创建带有置信度的属性
     *
     * @param text 原始文本
     * @param confidence 置信度
     * @return 实体属性
     */
    public static EntityAttributes withConfidence(String text, double confidence) {
        return builder()
                .originalText(text)
                .confidence(confidence)
                .build();
    }

    /**
     * 创建带有额外属性的实体属性
     *
     * @param text 原始文本
     * @param properties 额外属性
     * @return 实体属性
     */
    public static EntityAttributes withProperties(String text, Map<String, Object> properties) {
        return builder()
                .originalText(text)
                .properties(properties)
                .confidence(1.0)
                .build();
    }
}