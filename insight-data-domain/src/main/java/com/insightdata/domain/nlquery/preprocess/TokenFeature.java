package com.insightdata.domain.nlquery.preprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 分词特征
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenFeature {

    /**
     * 词汇相关特征
     */
    private LexicalFeature lexical;

    /**
     * 语法相关特征
     */
    private GrammaticalFeature grammatical;

    /**
     * 语义相关特征
     */
    private SemanticFeature semantic;

    /**
     * 统计相关特征
     */
    private StatisticalFeature statistical;

    /**
     * 自定义特征
     */
    @Builder.Default
    private Map<String, Object> customFeatures = new HashMap<>();

    /**
     * 词汇特征
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LexicalFeature {
        private String lemma;          // 词元
        private String stem;           // 词干
        private String pos;            // 词性
        private boolean stopWord;      // 是否停用词
        private boolean punctuation;   // 是否标点符号
        private boolean digit;         // 是否数字
        private boolean alphabetic;    // 是否字母
        private boolean chinese;       // 是否中文
    }

    /**
     * 语法特征
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GrammaticalFeature {
        private String dependency;     // 依存关系
        private String chunk;          // 语块类型
        private String syntacticRole;  // 句法角色
        private int depth;            // 语法树深度
        private boolean isRoot;       // 是否根节点
        private boolean isLeaf;       // 是否叶节点
    }

    /**
     * 语义特征
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SemanticFeature {
        private String wordNet;        // WordNet类别
        private String namedEntity;    // 命名实体类型
        private double sentiment;      // 情感倾向
        private Map<String, Double> topics; // 主题分布
        private Map<String, Double> embeddings; // 词向量
    }

    /**
     * 统计特征
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatisticalFeature {
        private int frequency;        // 词频
        private double tfidf;         // TF-IDF值
        private double entropy;       // 信息熵
        private double keywordScore;  // 关键词分数
        private Map<String, Double> cooccurrence; // 共现统计
    }

    /**
     * 添加自定义特征
     */
    public void addCustomFeature(String key, Object value) {
        if (customFeatures == null) {
            customFeatures = new HashMap<>();
        }
        customFeatures.put(key, value);
    }

    /**
     * 获取自定义特征
     */
    public Object getCustomFeature(String key) {
        return customFeatures != null ? customFeatures.get(key) : null;
    }

    /**
     * 移除自定义特征
     */
    public Object removeCustomFeature(String key) {
        return customFeatures != null ? customFeatures.remove(key) : null;
    }

    /**
     * 检查是否包含自定义特征
     */
    public boolean hasCustomFeature(String key) {
        return customFeatures != null && customFeatures.containsKey(key);
    }

    /**
     * 清空自定义特征
     */
    public void clearCustomFeatures() {
        if (customFeatures != null) {
            customFeatures.clear();
        }
    }
}