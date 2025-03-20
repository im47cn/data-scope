package com.insightdata.domain.nlquery.preprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分词特征
 */
@Data
@Builder
     * 词性
     */
    private String pos;

    /**
     * 词形
     */
    private String lemma;

    /**
     * 词根
     */
    private String stem;

    /**
     * 词频
     */
    private int frequency;

    /**
     * 词长
     */
    private int length;

    /**
     * 词的位置
     */
    private int position;

    /**
     * 词的语义类型
     */
    private String semanticType;

    /**
     * 词的依存关系
     */
    private String dependency;

    /**
     * 词的情感倾向
     */
    private double sentiment;

    /**
     * 词的语言
     */
    private String language;

    /**
     * 词的词形变化
     */
    private MorphologicalFeature morphological;

    /**
     * 词的词法特征
     */
    private LexicalFeature lexical;

    /**
     * 词的语义特征
     */
    private SemanticFeature semantic;

    /**
     * 词形变化特征
     */
    @Data
    @Builder
    public static class MorphologicalFeature {
        private String number;
        private String gender;
        private String tense;
        private String aspect;
        private String mood;
        private String voice;
        private String person;
        private String case_;
    }

    /**
     * 词法特征
     */
    @Data
    @Builder
    public static class LexicalFeature {
        private boolean isStopWord;
        private boolean isPunctuation;
        private boolean isDigit;
        private boolean isAlpha;
        private boolean isAlphaNumeric;
        private boolean isTitle;
        private boolean isUpper;
        private boolean isLower;
        private boolean isSpace;
    }

    /**
     * 语义特征
     */
    @Data
    @Builder
    public static class SemanticFeature {
        private String wordNet;
        private String hypernym;
        private String hyponym;
        private String synonym;
        private String antonym;
        private String meronym;
        private String holonym;
    }
}