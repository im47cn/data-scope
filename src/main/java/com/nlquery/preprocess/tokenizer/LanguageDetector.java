package com.nlquery.preprocess.tokenizer;

/**
 * 语言检测器接口
 * 负责检测文本的语言
 */
public interface LanguageDetector {
    
    /**
     * 检测语言
     *
     * @param text 输入文本
     * @return 语言代码（如"en"、"zh"等）
     */
    String detectLanguage(String text);
}
