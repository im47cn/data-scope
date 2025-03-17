package com.nlquery.preprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 预处理后的文本
 */
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
     * 标准化后的文本
     */
    private String normalizedText;
    
    /**
     * 分词结果
     */
    @Builder.Default
    private List<String> tokens = new ArrayList<>();
    
    /**
     * 词性标注结果
     */
    @Builder.Default
    private List<String> posTags = new ArrayList<>();
    
    /**
     * 命名实体识别结果
     */
    @Builder.Default
    private List<EntityTag> entities = new ArrayList<>();
    
    /**
     * 依存句法分析结果
     */
    @Builder.Default
    private List<String> dependencies = new ArrayList<>();
    
    /**
     * 语言
     */
    private String language;
    
    /**
     * 置信度分数(0-1)
     */
    private double confidence;
    
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 警告信息
     */
    @Builder.Default
    private List<String> warnings = new ArrayList<>();
    
    /**
     * 创建一个简单的预处理结果
     */
    public static PreprocessedText simple(String text) {
        return PreprocessedText.builder()
                .originalText(text)
                .normalizedText(text)
                .success(true)
                .confidence(1.0)
                .build();
    }
    
    /**
     * 创建一个失败的预处理结果
     */
    public static PreprocessedText failed(String text, String error) {
        return PreprocessedText.builder()
                .originalText(text)
                .success(false)
                .errorMessage(error)
                .confidence(0.0)
                .build();
    }
    
    /**
     * 创建一个带警告的预处理结果
     */
    public static PreprocessedText withWarnings(String text, List<String> warnings) {
        return PreprocessedText.builder()
                .originalText(text)
                .normalizedText(text)
                .success(true)
                .confidence(0.8)
                .warnings(warnings)
                .build();
    }
    
    /**
     * 添加实体
     */
    public PreprocessedText addEntity(EntityTag entity) {
        entities.add(entity);
        return this;
    }
    
    /**
     * 添加警告
     */
    public PreprocessedText addWarning(String warning) {
        warnings.add(warning);
        return this;
    }
    
    /**
     * 获取指定类型的实体
     */
    public List<EntityTag> getEntitiesByType(EntityType type) {
        List<EntityTag> result = new ArrayList<>();
        for (EntityTag entity : entities) {
            if (entity.getType() == type) {
                result.add(entity);
            }
        }
        return result;
    }
}
