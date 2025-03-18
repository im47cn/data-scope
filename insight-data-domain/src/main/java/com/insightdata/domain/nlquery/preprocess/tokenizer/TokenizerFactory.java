package com.insightdata.domain.nlquery.preprocess.tokenizer;

/**
 * 分词器工厂
 * 用于创建不同类型的分词器
 */
public class TokenizerFactory {
    
    /**
     * 创建默认分词器
     *
     * @return 默认分词器
     */
    public static Tokenizer createDefaultTokenizer() {
        return new HanlpTokenizer();
    }
    
    /**
     * 创建分词器
     *
     * @param language 语言
     * @return 分词器
     */
    public static Tokenizer createTokenizer(String language) {
        // 目前只有默认分词器，后续可以根据语言创建不同的分词器
        return createDefaultTokenizer();
    }
}
