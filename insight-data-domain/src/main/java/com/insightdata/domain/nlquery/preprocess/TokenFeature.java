package com.insightdata.domain.nlquery.preprocess;

import lombok.Builder;
import lombok.Data;

/**
 * Token features for NLP preprocessing
 */
@Data
@Builder
public class TokenFeature {
    
    private String token;                // 原始词
    private String normalizedToken;      // 标准化后的词
    private LexicalFeature lexical;      // 词法特征
    private GrammaticalFeature grammar;  // 语法特征
    private boolean isStopWord;          // 是否是停用词
    private boolean isCustomDictWord;    // 是否是自定义词典词
    private double confidence;           // 置信度
    
    /**
     * Lexical features of a token
     */
    @Data
    @Builder
    public static class LexicalFeature {
        private String lemma;        // 词元
        private String stem;         // 词干
        private String pos;          // 词性
        private String prefix;       // 前缀
        private String suffix;       // 后缀
        private boolean isCapital;   // 是否大写
        private boolean isNumeric;   // 是否数字
        private boolean isPunct;     // 是否标点
        
        /**
         * Create default lexical feature
         */
        public static LexicalFeature createDefault() {
            return LexicalFeature.builder()
                    .lemma("")
                    .stem("")
                    .pos("")
                    .prefix("")
                    .suffix("")
                    .isCapital(false)
                    .isNumeric(false)
                    .isPunct(false)
                    .build();
        }
    }
    
    /**
     * Create default token feature
     */
    public static TokenFeature createDefault(String token) {
        return TokenFeature.builder()
                .token(token)
                .normalizedToken(token.toLowerCase())
                .lexical(LexicalFeature.createDefault())
                .grammar(GrammaticalFeature.createDefault())
                .isStopWord(false)
                .isCustomDictWord(false)
                .confidence(1.0)
                .build();
    }
}