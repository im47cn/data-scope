package com.insightdata.domain.nlquery.preprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 词法特征
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenFeature {

    /**
     * 词元特征
     */
    private LexicalFeature lexical;

    /**
     * 语法特征
     */
    private GrammaticalDetails grammatical;

    /**
     * 语义特征
     */
    private SemanticFeature semantic;

    /**
     * 词元特征
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LexicalFeature {
        /**
         * 词元
         */
        private String lemma;

        /**
         * 词性
         */
        private String pos;

        /**
         * 词根
         */
        private String stem;

        /**
         * 是否停用词
         */
        private Boolean isStopWord;
    }

    /**
     * 语法特征
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GrammaticalDetails {
        /**
         * 依存关系
         */
        private String dependency;

        /**
         * 句法成分
         */
        private String syntacticComponent;

        /**
         * 语法功能
         */
        private String grammaticalFunction;
    }

    /**
     * 语义特征
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SemanticFeature {
        /**
         * 实体类型
         */
        private String entityType;

        /**
         * 语义角色
         */
        private String semanticRole;

        /**
         * 语义类别
         */
        private String semanticCategory;

        /**
         * 语义关系
         */
        private String semanticRelation;
    }
}