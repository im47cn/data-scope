package com.insightdata.domain.nlquery.preprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Semantic features of a token
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemanticFeature {
    /**
     * Named entity type
     */
    private String entityType;

    /**
     * Word sense
     */
    private String wordSense;

    /**
     * Semantic role
     */
    private String semanticRole;

    /**
     * Sentiment score
     */
    private Double sentimentScore;

    /**
     * Topic categories
     */
    private List<String> topics;

    /**
     * Domain-specific concepts
     */
    private List<String> concepts;

    /**
     * Synonyms
     */
    private List<String> synonyms;

    /**
     * Hypernyms (more general terms)
     */
    private List<String> hypernyms;

    /**
     * Hyponyms (more specific terms)
     */
    private List<String> hyponyms;

    /**
     * Related terms
     */
    private List<String> relatedTerms;

    /**
     * Domain-specific context
     */
    private String domain;

    /**
     * Semantic similarity score with query context
     */
    private Double contextSimilarity;
}