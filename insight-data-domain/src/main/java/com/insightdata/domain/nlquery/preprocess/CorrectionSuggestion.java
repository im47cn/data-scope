package com.insightdata.domain.nlquery.preprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a correction suggestion for text preprocessing
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorrectionSuggestion {

    /**
     * Original text that needs correction
     */
    private String originalText;

    /**
     * Suggested correction text
     */
    private String suggestedText;

    /**
     * Type of correction
     */
    private CorrectionType type;

    /**
     * Start position in original text
     */
    private int startPosition;

    /**
     * End position in original text
     */
    private int endPosition;

    /**
     * Confidence score of this suggestion (0-1)
     */
    private double confidence;

    /**
     * Factory method for spelling correction
     */
    public static CorrectionSuggestion spelling(String original, String suggested, double confidence) {
        return CorrectionSuggestion.builder()
                .originalText(original)
                .suggestedText(suggested)
                .type(CorrectionType.SPELLING)
                .confidence(confidence)
                .build();
    }

    /**
     * Factory method for grammar correction
     */
    public static CorrectionSuggestion grammar(String original, String suggested, double confidence) {
        return CorrectionSuggestion.builder()
                .originalText(original)
                .suggestedText(suggested)
                .type(CorrectionType.GRAMMAR)
                .confidence(confidence)
                .build();
    }

    /**
     * Factory method for semantic correction
     */
    public static CorrectionSuggestion semantic(String original, String suggested, double confidence) {
        return CorrectionSuggestion.builder()
                .originalText(original)
                .suggestedText(suggested)
                .type(CorrectionType.SEMANTIC)
                .confidence(confidence)
                .build();
    }
}
