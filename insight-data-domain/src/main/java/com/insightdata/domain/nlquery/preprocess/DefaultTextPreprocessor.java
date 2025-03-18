package com.insightdata.domain.nlquery.preprocess;

public class DefaultTextPreprocessor implements TextPreprocessor{
    @Override
    public PreprocessedText preprocess(String text) {
        // TODO: Implement actual text preprocessing logic here.
        // Just return a builder with the text for now
        return PreprocessedText.builder().text(text).build();
    }
}
