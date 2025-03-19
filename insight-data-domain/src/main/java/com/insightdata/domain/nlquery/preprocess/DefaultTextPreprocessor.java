package com.insightdata.domain.nlquery.preprocess;

public class DefaultTextPreprocessor implements TextPreprocessor{
    @Override
    public PreprocessedText preprocess(String text) {
        // TODO: Implement actual text preprocessing logic here.
        // Just return a builder with the original text for now
        return PreprocessedText.builder()
                .originalText(text)
                .normalizedText(text)  // 简单实现：标准化文本暂时同原始文本
                .build();
    }
}
