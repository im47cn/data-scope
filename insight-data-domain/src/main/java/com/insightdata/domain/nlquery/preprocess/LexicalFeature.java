package com.insightdata.domain.nlquery.preprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lexical features of a token
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LexicalFeature {

    /**
     * Part of speech tag
     */
    private String posTag;

    /**
     * Detailed part of speech tag
     */
    private String detailedPosTag;

    /**
     * Lemma (base/dictionary form)
     */
    private String lemma;

    /**
     * Stem (root form)
     */
    private String stem;

    /**
     * Original token text
     */
    private String text;

    /**
     * Normalized form
     */
    private String normalizedForm;

    /**
     * Character length
     */
    private Integer length;

    /**
     * Is punctuation
     */
    private Boolean isPunctuation;

    /**
     * Is stop word
     */
    private Boolean isStopWord;

    /**
     * Is digit
     */
    private Boolean isDigit;

    /**
     * Is alphabetic
     */
    private Boolean isAlphabetic;

    /**
     * Is title case
     */
    private Boolean isTitleCase;

    /**
     * Is upper case
     */
    private Boolean isUpperCase;

    /**
     * Is lower case
     */
    private Boolean isLowerCase;

    /**
     * Contains digits
     */
    private Boolean containsDigits;

    /**
     * Contains punctuation
     */
    private Boolean containsPunctuation;

    /**
     * Contains special characters
     */
    private Boolean containsSpecialChars;

    /**
     * Word shape (e.g., Xxxxx, XXX, 999)
     */
    private String wordShape;

    /**
     * Prefix (first n characters)
     */
    private String prefix;

    /**
     * Suffix (last n characters)
     */
    private String suffix;
}