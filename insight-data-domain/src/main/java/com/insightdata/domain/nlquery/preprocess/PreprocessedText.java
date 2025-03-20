package com.insightdata.domain.nlquery.preprocess;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents preprocessed text with various features and corrections
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreprocessedText {

    /**
     * Original input text
     */
    private String originalText;

    /**
     * Normalized text after basic preprocessing
     */
    private String normalizedText;

    /**
     * Tokenized text
     */
    private List<String> tokens;

    /**
     * Token features including lexical, grammatical and semantic information
     */
    private Map<String, TokenFeature> tokenFeatures;

    /**
     * Suggested corrections for potential errors
     */
    private List<CorrectionSuggestion> corrections;

    /**
     * Confidence score of preprocessing (0-1)
     */
    private double confidence;
}
