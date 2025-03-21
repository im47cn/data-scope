package com.insightdata.domain.nlquery.preprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Token特征
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenFeature {

    /**
     * Token文本
     */
    private String token;

    /**
     * Token长度
     */
    private int length;

    /**
     * 词法特征
     */
    private LexicalFeature lexical;

    /**
     * 语义特征
     */
    private SemanticFeature semantic;

    /**
     * 词法特征
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LexicalFeature {
        private String pos;  // 词性
        private String lemma;  // 词根
        private boolean isStopWord;  // 是否为停用词
        private int length;  // 长度
        private boolean containsDigit;  // 是否包含数字
        private boolean containsPunctuation;  // 是否包含标点
    }

    /**
     * 语义特征
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SemanticFeature {
        private String wordVector;  // 词向量
        private double similarity;  // 相似度
        private String category;  // 语义类别
    }
}