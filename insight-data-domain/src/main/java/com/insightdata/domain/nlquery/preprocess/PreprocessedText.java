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
     * 标准化后的文本
     */
    private String normalizedText;

    /**
     * 分词结果
     */
    private List<String> tokens;

    /**
     * 语言
     */
    private String language;

    /**
     * Token特征
     */
    private Map<String, TokenFeature> tokenFeatures;

    /**
     * 纠正建议
     */
    private List<CorrectionSuggestion> corrections;
}
