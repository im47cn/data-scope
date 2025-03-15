package com.insightdata.nlquery.preprocess;

import java.util.List;

/**
 * 拼写纠正建议
 * 表示对拼写错误的纠正建议
 */
public class CorrectionSuggestion {
    
    // 原始单词
    private String originalWord;
    
    // 建议的单词列表
    private List<String> suggestions;
    
    // 最佳建议
    private String bestSuggestion;
    
    // 置信度
    private double confidence;
    
    /**
     * 构造函数
     *
     * @param originalWord 原始单词
     * @param suggestions 建议的单词列表
     */
    public CorrectionSuggestion(String originalWord, List<String> suggestions) {
        this.originalWord = originalWord;
        this.suggestions = suggestions;
        if (suggestions != null && !suggestions.isEmpty()) {
            this.bestSuggestion = suggestions.get(0);
            this.confidence = 0.8;
        }
    }
    
    /**
     * 构造函数
     *
     * @param originalWord 原始单词
     * @param suggestions 建议的单词列表
     * @param bestSuggestion 最佳建议
     * @param confidence 置信度
     */
    public CorrectionSuggestion(String originalWord, List<String> suggestions, String bestSuggestion, double confidence) {
        this.originalWord = originalWord;
        this.suggestions = suggestions;
        this.bestSuggestion = bestSuggestion;
        this.confidence = confidence;
    }
    
    /**
     * 获取原始单词
     *
     * @return 原始单词
     */
    public String getOriginalWord() {
        return originalWord;
    }
    
    /**
     * 设置原始单词
     *
     * @param originalWord 原始单词
     */
    public void setOriginalWord(String originalWord) {
        this.originalWord = originalWord;
    }
    
    /**
     * 获取建议的单词列表
     *
     * @return 建议的单词列表
     */
    public List<String> getSuggestions() {
        return suggestions;
    }
    
    /**
     * 设置建议的单词列表
     *
     * @param suggestions 建议的单词列表
     */
    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }
    
    /**
     * 获取最佳建议
     *
     * @return 最佳建议
     */
    public String getBestSuggestion() {
        return bestSuggestion;
    }
    
    /**
     * 设置最佳建议
     *
     * @param bestSuggestion 最佳建议
     */
    public void setBestSuggestion(String bestSuggestion) {
        this.bestSuggestion = bestSuggestion;
    }
    
    /**
     * 获取置信度
     *
     * @return 置信度
     */
    public double getConfidence() {
        return confidence;
    }
    
    /**
     * 设置置信度
     *
     * @param confidence 置信度
     */
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
    
    @Override
    public String toString() {
        return originalWord + " -> " + bestSuggestion + " (" + confidence + ")";
    }
}
