package com.insightdata.domain.nlquery.preprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Features extracted from a token during text preprocessing
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenFeature {

    /**
     * Lexical features of the token
     */
    private LexicalFeature lexical;

    /**
     * Grammatical features of the token
     */
    private GrammaticalFeature grammatical;

    /**
     * Semantic features of the token
     */
    private SemanticFeature semantic;

    /**
     * Lexical features like part of speech, lemma etc.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LexicalFeature {
        private String lemma;
        private String pos;
        private String stem;
        private boolean isStopWord;
        private boolean isNegation;
    }

    /**
     * Grammatical features like dependency relations
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GrammaticalFeature {
        private String dependencyRelation;
        private String dependencyHead;
        private String morphology;
        private String syntacticRole;
    }

    /**
     * Semantic features like named entities, concepts etc.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SemanticFeature {
        private String namedEntityType;
        private String concept;
        private String domain;
        private double confidence;
    }
}