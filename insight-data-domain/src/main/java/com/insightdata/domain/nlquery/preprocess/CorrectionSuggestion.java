package com.insightdata.domain.nlquery.preprocess;

import lombok.Builder;
import lombok.Data;

/**
 * 纠错建议
 */
@Data
@Builder
public class CorrectionSuggestion {

    /**
     * 原始文本
     */
    private String originalText;

    /**
     * 建议的修正文本
     */
    private String suggestedText;

    /**
     * 开始位置
     */
    private int startPosition;

    /**
     * 结束位置
     */
    private int endPosition;

    /**
     * 置信度
     */
    private double confidence;

    /**
     * 错误类型
     */
    private ErrorType errorType;

    /**
     * 错误原因描述
     */
    private String reason;

    /**
     * 错误类型枚举
     */
    public enum ErrorType {
        /**
         * 拼写错误
         */
        SPELLING,

        /**
         * 语法错误
         */
        GRAMMAR,

        /**
         * 标点符号错误
         */
        PUNCTUATION,

        /**
         * 大小写错误
         */
        CAPITALIZATION,

        /**
         * 重复错误
         */
        REDUNDANCY,

        /**
         * 缺失错误
         */
        MISSING,

        /**
         * 其他错误
         */
        OTHER
    }

    /**
     * 创建一个基本的纠错建议
     */
    public static CorrectionSuggestion basic(String originalText, String suggestedText) {
        return CorrectionSuggestion.builder()
                .originalText(originalText)
                .suggestedText(suggestedText)
                .confidence(1.0)
                .errorType(ErrorType.OTHER)
                .build();
    }

    /**
     * 创建一个带位置的纠错建议
     */
    public static CorrectionSuggestion withPosition(String originalText, String suggestedText,
            int startPosition, int endPosition) {
        return CorrectionSuggestion.builder()
                .originalText(originalText)
                .suggestedText(suggestedText)
                .startPosition(startPosition)
                .endPosition(endPosition)
                .confidence(1.0)
                .errorType(ErrorType.OTHER)
                .build();
    }

    /**
     * 创建一个完整的纠错建议
     */
    public static CorrectionSuggestion complete(String originalText, String suggestedText,
            int startPosition, int endPosition, double confidence,
            ErrorType errorType, String reason) {
        return CorrectionSuggestion.builder()
                .originalText(originalText)
                .suggestedText(suggestedText)
                .startPosition(startPosition)
                .endPosition(endPosition)
                .confidence(confidence)
                .errorType(errorType)
                .reason(reason)
                .build();
    }
}
