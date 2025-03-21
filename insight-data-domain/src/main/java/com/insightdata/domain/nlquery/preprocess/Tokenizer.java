package com.insightdata.domain.nlquery.preprocess;

import java.util.List;

/**
 * 分词器接口
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
     * 分词(带语言参数)
     *
     * @param text 输入文本
     * @param language 语言代码
     * @return 分词结果
     */
    default List<String> tokenize(String text, String language) {
        return tokenize(text);
    }
}