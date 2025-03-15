package com.insightdata.nlquery.preprocess;

/**
 * 文本预处理器接口
 * 负责对输入的自然语言查询进行预处理，为后续的意图识别和实体提取提供基础
 */
public interface TextPreprocessor {
    
    /**
     * 预处理文本
     *
     * @param text 输入文本
     * @return 预处理后的文本
     */
    PreprocessedText preprocess(String text);
    
    /**
     * 预处理文本，带上下文
     *
     * @param text 输入文本
     * @param context 上下文信息
     * @return 预处理后的文本
     */
    PreprocessedText preprocess(String text, PreprocessContext context);
}
