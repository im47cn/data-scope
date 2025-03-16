package com.insightdata.nlquery.preprocess;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * 拼写纠正建议
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CorrectionSuggestion {
    
    /**
     * 原始文本
     */
    String originalText;
    
    /**
     * 建议文本
     */
    String suggestedText;
    
    /**
     * 开始位置
     */
    @Builder.Default
    int startPosition = 0;
    
    /**
     * 结束位置
     */
    @Builder.Default
    int endPosition = 0;
    
    /**
     * 置信度分数(0-1)
     */
    @Builder.Default
    double confidence = 1.0;
    
    /**
     * 纠正类型
     */
    String correctionType;
    
    /**
     * 纠正说明
     */
    String explanation;
    
    /**
     * 创建一个简单的纠正建议
     */
    public CorrectionSuggestion(String originalText, String suggestedText) {
        this.originalText = originalText;
        this.suggestedText = suggestedText;
        this.confidence = 1.0;
    }
    
    /**
     * 创建一个带位置信息的纠正建议
     */
    public CorrectionSuggestion(String originalText, String suggestedText, int startPosition, int endPosition) {
        this(originalText, suggestedText);
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    /**
     * 创建一个完整的纠正建议
     */
    public CorrectionSuggestion(String originalText, String suggestedText, int startPosition, int endPosition, double confidence) {
        this(originalText, suggestedText, startPosition, endPosition);
        this.confidence = confidence;
    }
}
