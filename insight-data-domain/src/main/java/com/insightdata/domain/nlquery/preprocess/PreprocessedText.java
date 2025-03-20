package com.insightdata.domain.nlquery.preprocess;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 预处理后的文本
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreprocessedText {
    /**
     * 原始文本
     */
    private String originalText;

    /**
     * 规范化后的文本
     */
    private String normalizedText;

    /**
     * 分词结果
     */
    private List<String> tokens;

    /**
     * 分词特征
     */
    private Map<String, TokenFeature> tokenFeatures;

    /**
     * 纠错建议
     */
    private List<CorrectionSuggestion> correctionSuggestions;

    /**
     * 语言
     */
    private String language;

    /**
     * 处理时间(毫秒)
     */
    private Long processingTime;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 错误信息
     */
    private String errorMessage;
}
