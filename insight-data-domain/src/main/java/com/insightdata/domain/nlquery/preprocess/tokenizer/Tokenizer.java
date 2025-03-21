package com.insightdata.domain.nlquery.preprocess.tokenizer;

import java.util.List;

/**
 * 分词器接口
 */
public interface Tokenizer {

    /**
     * 对文本进行分词
     *
     * @param text 输入文本
     * @param language 语言代码
     * @return 分词结果
     */
    List<String> tokenize(String text, String language);

    /**
     * 对文本进行分词(使用默认语言)
     *
     * @param text 输入文本
     * @return 分词结果
     */
    default List<String> tokenize(String text) {
        return tokenize(text, "zh-CN");
    }
}
