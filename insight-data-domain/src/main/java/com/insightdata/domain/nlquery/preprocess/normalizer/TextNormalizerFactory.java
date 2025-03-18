package com.insightdata.domain.nlquery.preprocess.normalizer;

/**
 * 文本标准化器工厂
 * 用于创建不同类型的文本标准化器
 */
public class TextNormalizerFactory {
    
    /**
     * 创建默认文本标准化器
     *
     * @return 默认文本标准化器
     */
    public static TextNormalizer createDefaultNormalizer() {
        return new DefaultTextNormalizer();
    }
    
    /**
     * 创建SQL查询文本标准化器
     *
     * @return SQL查询文本标准化器
     */
    public static TextNormalizer createSqlQueryNormalizer() {
        return new SqlQueryTextNormalizer();
    }
    
    /**
     * 创建中文文本标准化器
     *
     * @return 中文文本标准化器
     */
    public static TextNormalizer createChineseNormalizer() {
        return new ChineseTextNormalizer();
    }
    
    /**
     * 根据语言创建文本标准化器
     *
     * @param language 语言
     * @return 文本标准化器
     */
    public static TextNormalizer createNormalizer(String language) {
        if (language == null || language.isEmpty()) {
            return createDefaultNormalizer();
        }
        
        switch (language.toLowerCase()) {
            case "zh":
            case "zh-cn":
            case "zh-tw":
                return createChineseNormalizer();
            case "sql":
                return createSqlQueryNormalizer();
            default:
                return createDefaultNormalizer();
        }
    }
}
