package com.nlquery.preprocess;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreprocessedText {
    
    /**
     * 原始文本
     */
    private String originalText;
    
    /**
     * 标准化文本
     */
    private String normalizedText;
    
    /**
     * 分词结果
     */
    private List<String> tokens;
    
    /**
     * 词性标注
     */
    private List<PosTag> posTags;
    
    /**
     * 语言
     */
    private String language;
    
    /**
     * 纠错建议
     */
    private List<CorrectionSuggestion> corrections;
    
    /**
     * 分词置信度
     */
    private Map<String, Double> tokenConfidences;
    
    /**
     * 词性标注置信度
     */
    private Map<String, Double> posTagConfidences;
    
    /**
     * 预处理上下文
     */
    private PreprocessContext context;
    
    /**
     * 是否需要纠错
     */
    private Boolean needsCorrection;
    
    /**
     * 是否已纠错
     */
    private Boolean isCorrected;
    
    /**
     * 纠错消息
     */
    private String correctionMessage;
    
    /**
     * 是否成功
     */
    private Boolean success;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 获取原始文本
     */
    public String getOriginalText() {
        return originalText;
    }
    
    /**
     * 获取标准化文本
     */
    public String getNormalizedText() {
        return normalizedText;
    }
    
    /**
     * 获取分词结果
     */
    public List<String> getTokens() {
        return tokens;
    }
    
    /**
     * 获取语言
     */
    public String getLanguage() {
        return language;
    }
    
    /**
     * 设置语言
     */
    public void setLanguage(String language) {
        this.language = language;
    }
    
    /**
     * 是否成功
     */
    public boolean isSuccess() {
        return Boolean.TRUE.equals(success);
    }
    
    /**
     * 获取错误信息
     */
    public String getErrorMessage() {
        return errorMessage != null ? errorMessage : "";
    }
}
