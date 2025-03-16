package com.insightdata.nlquery.preprocess;

/**
 * 文本预处理器接口
 */
public interface TextPreprocessor {
    
    /**
     * 预处理文本
     * 
     * @param text 原始文本
     * @return 预处理结果
     */
    PreprocessedText preprocess(String text);
    
    /**
     * 预处理文本(带上下文)
     * 
     * @param text 原始文本
     * @param context 预处理上下文
     * @return 预处理结果
     */
    PreprocessedText preprocess(String text, PreprocessContext context);
    
    /**
     * 获取支持的语言列表
     * 
     * @return 支持的语言列表
     */
    String[] getSupportedLanguages();
    
    /**
     * 是否支持指定语言
     * 
     * @param language 语言代码
     * @return 是否支持
     */
    default boolean supportsLanguage(String language) {
        if (language == null) {
            return false;
        }
        String[] supported = getSupportedLanguages();
        if (supported == null) {
            return false;
        }
        for (String lang : supported) {
            if (language.equalsIgnoreCase(lang)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 获取默认语言
     * 
     * @return 默认语言
     */
    default String getDefaultLanguage() {
        return "zh-CN";
    }
    
    /**
     * 获取最小置信度
     * 
     * @return 最小置信度
     */
    default double getMinConfidence() {
        return 0.6;
    }
    
    /**
     * 是否使用模糊匹配
     * 
     * @return 是否使用模糊匹配
     */
    default boolean useFuzzyMatching() {
        return true;
    }
    
    /**
     * 是否使用缓存
     * 
     * @return 是否使用缓存
     */
    default boolean useCache() {
        return true;
    }
    
    /**
     * 获取缓存过期时间(秒)
     * 
     * @return 缓存过期时间
     */
    default int getCacheExpireSeconds() {
        return 300;
    }
}
