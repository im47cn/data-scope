package com.insightdata.domain.nlquery.preprocess;

/**
 * Types of text corrections that can be suggested
 */
public enum CorrectionType {
    /**
     * Spelling correction
     */
    SPELLING,

    /**
     * Grammar correction
     */
    GRAMMAR,

    /**
     * Semantic correction
     */
    SEMANTIC,

    /**
     * Punctuation correction
     */
    PUNCTUATION,

    /**
     * Word choice correction
     */
    WORD_CHOICE,

    /**
     * Word order correction
     */
    WORD_ORDER,

    /**
     * Other types of corrections
     */
    OTHER
}
