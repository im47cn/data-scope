package com.nlquery.preprocess.normalizer;

/**
 * 文本标准化器接口
 * 负责对输入的文本进行标准化处理
 */
public interface TextNormalizer {
    
    /**
     * 标准化文本
     *
     * @param text 输入文本
     * @return 标准化后的文本
     */
    String normalize(String text);
}
