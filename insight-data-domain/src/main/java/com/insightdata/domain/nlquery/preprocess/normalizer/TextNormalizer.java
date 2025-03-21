package com.insightdata.domain.nlquery.preprocess.normalizer;

/**
 * 文本标准化接口
 */
public interface TextNormalizer {

    /**
     * 标准化文本
     *
     * @param text 原始文本
     * @param language 语言代码
     * @return 标准化后的文本
     */
    String normalize(String text, String language);

    /**
     * 标准化文本(使用默认语言)
     *
     * @param text 原始文本
     * @return 标准化后的文本
     */
    default String normalize(String text) {
        return normalize(text, "zh-CN");
    }

    /**
     * 获取配置
     *
     * @return 标准化配置
     */
    NormalizerConfig getConfig();

    /**
     * 设置配置
     *
     * @param config 标准化配置
     */
    void setConfig(NormalizerConfig config);
}
