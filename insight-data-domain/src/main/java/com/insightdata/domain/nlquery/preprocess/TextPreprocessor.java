package com.insightdata.domain.nlquery.preprocess;

/**
 * 文本预处理器接口
 */
public interface TextPreprocessor {

    /**
     * 预处理文本
     *
     * @param text 原始文本
     * @return 预处理结果
     */
    PreprocessedText preprocess(String text);

    /**
     * 预处理文本(带语言参数)
     *
     * @param text 原始文本
     * @param language 语言代码
     * @return 预处理结果
     */
    default PreprocessedText preprocess(String text, String language) {
        PreprocessedText result = preprocess(text);
        return PreprocessedText.builder()
                .originalText(result.getOriginalText())
                .normalizedText(result.getNormalizedText())
                .tokens(result.getTokens())
                .language(language)
                .tokenFeatures(result.getTokenFeatures())
                .corrections(result.getCorrections())
                .build();
    }
}
