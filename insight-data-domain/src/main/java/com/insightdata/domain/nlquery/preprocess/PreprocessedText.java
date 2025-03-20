package com.insightdata.domain.nlquery.preprocess;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Preprocessed text result
 */
@Data
@Builder
public class PreprocessedText {

    private String originalText;         // 原始文本
    private String normalizedText;       // 标准化文本
    private List<String> tokens;         // 分词结果
    private List<TokenFeature> features; // 词特征
    private Language language;           // 语言
    private double confidence;           // 置信度
    private List<CorrectionSuggestion> corrections; // 纠错建议
    
    /**
     * Language type
     */
    public enum Language {
        CHINESE,    // 中文
        ENGLISH,    // 英文
        MIXED,      // 混合语言
        UNKNOWN     // 未知语言
    }
    
    /**
     * Create empty result
     */
    public static PreprocessedText empty() {
        return PreprocessedText.builder()
                .originalText("")
                .normalizedText("")
                .language(Language.UNKNOWN)
                .confidence(0.0)
                .build();
    }
    
    /**
     * Create error result
     */
    public static PreprocessedText error(String message) {
        return PreprocessedText.builder()
                .originalText("")
                .normalizedText("")
                .language(Language.UNKNOWN)
                .confidence(0.0)
                .corrections(List.of(CorrectionSuggestion.error(message)))
                .build();
    }
}
