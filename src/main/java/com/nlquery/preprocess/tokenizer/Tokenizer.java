package com.nlquery.preprocess.tokenizer;

import java.util.List;

/**
 * 分词器接口
 * 负责将文本分割为单词或词组
 */
public interface Tokenizer {
    
    /**
     * 分词
     *
     * @param text 输入文本
     * @return 分词结果
     */
    List<String> tokenize(String text);
    
    /**
     * 分词
     *
     * @param text 输入文本
     * @param language 语言
     * @return 分词结果
     */
    List<String> tokenize(String text, String language);
}
