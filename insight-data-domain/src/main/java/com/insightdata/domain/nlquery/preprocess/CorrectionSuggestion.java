package com.insightdata.domain.nlquery.preprocess;

import lombok.Builder;
import lombok.Data;

/**
 * Correction suggestion for text preprocessing
 */
@Data
@Builder
public class CorrectionSuggestion {
    
    private String originalText;      // 原始文本
    private String suggestedText;     // 建议文本
    private CorrectionType type;      // 纠错类型
    private double confidence;        // 置信度
    private String description;       // 错误描述
    
    /**
     * Type of correction
     */
    public enum CorrectionType {
        SPELLING,       // 拼写错误
        GRAMMAR,        // 语法错误
        PUNCTUATION,   // 标点错误
        WORD_CHOICE,   // 用词错误
        ERROR,         // 其他错误
        NONE          // 无需纠正
    }
    
    /**
     * Create a spelling correction suggestion
     */
    public static CorrectionSuggestion spelling(String original, String suggested) {
        return CorrectionSuggestion.builder()
                .originalText(original)
                .suggestedText(suggested)
                .type(CorrectionType.SPELLING)
                .confidence(0.8)
                .description("可能存在拼写错误")
                .build();
    }
    
    /**
     * Create a grammar correction suggestion
     */
    public static CorrectionSuggestion grammar(String original, String suggested) {
        return CorrectionSuggestion.builder()
                .originalText(original)
                .suggestedText(suggested)
                .type(CorrectionType.GRAMMAR)
                .confidence(0.7)
                .description("可能存在语法错误")
                .build();
    }
    
    /**
     * Create an error suggestion
     */
    public static CorrectionSuggestion error(String error) {
        return CorrectionSuggestion.builder()
                .originalText("")
                .suggestedText("")
                .type(CorrectionType.ERROR)
                .confidence(1.0)
                .description(error)
                .build();
    }
}
