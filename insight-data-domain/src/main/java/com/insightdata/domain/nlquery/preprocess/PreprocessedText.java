package com.insightdata.domain.nlquery.preprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 预处理后的文本
 * 包含原始文本、标准化文本、分词结果等信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreprocessedText {

    public List<String> getTokens() {
        return tokens;
    }
    
    /**
     * 原始文本
     */
    private String originalText;
    
    /**
     * 标准化后的文本
     */
    private String normalizedText;
    
    /**
     * 分词结果
     */
    @Builder.Default
    private List<String> tokens = new ArrayList<>();
    
    /**
     * 文本语言
     */
    private String language;
    
    /**
     * Token特征信息
     */
    @Builder.Default
    private Map<String, TokenFeature> tokenFeatures = new HashMap<>();
    
    /**
     * 纠正建议列表
     */
    @Builder.Default
    private List<CorrectionSuggestion> corrections = new ArrayList<>();

    /**
     * 获取指定类型的纠正建议文本
     *
     * @param type 纠正类型
     * @return 纠正后的文本,如无纠正建议则返回null
     */
    public String getCorrectedText(CorrectionType type) {
        return corrections.stream()
                .filter(c -> c != null && c.getSuggestedText() != null)
                .filter(c -> c.getType() == type)
                .map(CorrectionSuggestion::getSuggestedText)
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取指定纠正类型的所有建议
     *
     * @param type 纠正类型
     * @return 纠正建议列表
     */
    public List<CorrectionSuggestion> getCorrections(CorrectionType type) {
        if (corrections == null) {
            return new ArrayList<>();
        }
        return corrections.stream()
                .filter(c -> c.getType() == type)
                .collect(Collectors.toList());
    }

    /**
     * 获取指定级别的所有纠正建议
     *
     * @param level 纠正级别
     * @return 纠正建议列表
     */
    public List<CorrectionSuggestion> getCorrectionsByLevel(CorrectionLevel level) {
        if (corrections == null) {
            return new ArrayList<>();
        }
        return corrections.stream()
                .filter(c -> c.getLevel() == level)
                .collect(Collectors.toList());
    }

    /**
     * 获取指定token的特征信息
     *
     * @param token 待查询的token
     * @return token的特征信息,如不存在则返回null
     */
    public TokenFeature getTokenFeature(String token) {
        return tokenFeatures != null ? tokenFeatures.get(token) : null;
    }

    /**
     * 添加纠正建议
     *
     * @param correction 待添加的纠正建议
     */
    public void addCorrection(CorrectionSuggestion correction) {
        if (corrections == null) {
            corrections = new ArrayList<>();
        }
        corrections.add(correction);
    }

    /**
     * 添加token特征
     *
     * @param token token文本
     * @param feature token特征
     */
    public void addTokenFeature(String token, TokenFeature feature) {
        if (tokenFeatures == null) {
            tokenFeatures = new HashMap<>();
        }
        tokenFeatures.put(token, feature);
    }
}
