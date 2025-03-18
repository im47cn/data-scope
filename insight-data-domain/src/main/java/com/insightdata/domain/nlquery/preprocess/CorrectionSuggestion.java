package com.insightdata.domain.nlquery.preprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文本纠正建议
 * 包含纠正类型、建议文本等信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorrectionSuggestion {

    /**
     * 原始文本
     */
    private String originalText;

    /**
     * 建议替换文本
     */
    private String suggestedText;

    /**
     * 纠正类型
     */
    private CorrectionType type;

    /**
     * 纠正级别
     */
    private CorrectionLevel level;

    /**
     * 纠正原因说明
     */
    private String reason;

    /**
     * 纠正的置信度(0-1)
     */
    private double confidence;

    /**
     * 在文本中的开始位置
     */
    private int startOffset;

    /**
     * 在文本中的结束位置
     */
    private int endOffset;

    /**
     * 创建拼写纠正建议
     *
     * @param original 原始文本
     * @param suggested 建议文本
     * @param confidence 置信度
     * @return 拼写纠正建议
     */
    public static CorrectionSuggestion spelling(String original, String suggested, double confidence) {
        return builder()
                .originalText(original)
                .suggestedText(suggested)
                .type(CorrectionType.SPELLING)
                .level(CorrectionLevel.CRITICAL)
                .confidence(confidence)
                .build();
    }

    /**
     * 创建语法纠正建议
     *
     * @param original 原始文本
     * @param suggested 建议文本
     * @param confidence 置信度
     * @return 语法纠正建议
     */
    public static CorrectionSuggestion grammar(String original, String suggested, double confidence) {
        return builder()
                .originalText(original)
                .suggestedText(suggested)
                .type(CorrectionType.GRAMMAR)
                .level(CorrectionLevel.WARNING)
                .confidence(confidence)
                .build();
    }

    /**
     * 创建格式纠正建议
     *
     * @param original 原始文本
     * @param suggested 建议文本
     * @param confidence 置信度
     * @return 格式纠正建议
     */
    public static CorrectionSuggestion format(String original, String suggested, double confidence) {
        return builder()
                .originalText(original)
                .suggestedText(suggested)
                .type(CorrectionType.FORMAT)
                .level(CorrectionLevel.SUGGESTION)
                .confidence(confidence)
                .build();
    }

    /**
     * 判断是否为严重程度的纠正
     *
     * @return 如果是严重级别的纠正返回true
     */
    public boolean isCritical() {
        return level == CorrectionLevel.CRITICAL;
    }

    /**
     * 判断是否为警告程度的纠正
     *
     * @return 如果是警告级别的纠正返回true
     */
    public boolean isWarning() {
        return level == CorrectionLevel.WARNING;
    }

    /**
     * 判断是否为建议程度的纠正
     *
     * @return 如果是建议级别的纠正返回true
     */
    public boolean isSuggestion() {
        return level == CorrectionLevel.SUGGESTION;
    }
}
