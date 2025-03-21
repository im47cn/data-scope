package com.insightdata.domain.nlquery.preprocess.language;

/**
 * 语言检测器接口
 */
public interface LanguageDetector {

    /**
     * 检测文本语言
     *
     * @param text 输入文本
     * @return 语言代码(如zh-CN, en-US等)
     */
    String detectLanguage(String text);
}