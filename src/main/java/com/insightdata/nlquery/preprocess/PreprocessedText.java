package com.insightdata.nlquery.preprocess;

import java.util.ArrayList;
import java.util.List;

/**
 * 预处理后的文本
 * 包含预处理后的文本信息
 */
public class PreprocessedText {
    
    // 原始文本
    private String originalText;
    
    // 标准化后的文本
    private String normalizedText;
    
    // 分词结果
    private List<String> tokens;
    
    // 词性标注结果
    private List<PosTag> posTags;
    
    // 实体标注结果
    private List<EntityTag> entityTags;
    
    // 纠正建议
    private List<CorrectionSuggestion> correctionSuggestions;
    
    // 语言
    private String language;
    
    /**
     * 默认构造函数
     */
    public PreprocessedText() {
        this.tokens = new ArrayList<>();
        this.posTags = new ArrayList<>();
        this.entityTags = new ArrayList<>();
        this.correctionSuggestions = new ArrayList<>();
    }
    
    /**
     * 构造函数
     *
     * @param originalText 原始文本
     * @param normalizedText 标准化后的文本
     */
    public PreprocessedText(String originalText, String normalizedText) {
        this();
        this.originalText = originalText;
        this.normalizedText = normalizedText;
    }
    
    /**
     * 获取原始文本
     *
     * @return 原始文本
     */
    public String getOriginalText() {
        return originalText;
    }
    
    /**
     * 设置原始文本
     *
     * @param originalText 原始文本
     */
    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }
    
    /**
     * 获取标准化后的文本
     *
     * @return 标准化后的文本
     */
    public String getNormalizedText() {
        return normalizedText;
    }
    
    /**
     * 设置标准化后的文本
     *
     * @param normalizedText 标准化后的文本
     */
    public void setNormalizedText(String normalizedText) {
        this.normalizedText = normalizedText;
    }
    
    /**
     * 获取分词结果
     *
     * @return 分词结果
     */
    public List<String> getTokens() {
        return tokens;
    }
    
    /**
     * 设置分词结果
     *
     * @param tokens 分词结果
     */
    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }
    
    /**
     * 获取词性标注结果
     *
     * @return 词性标注结果
     */
    public List<PosTag> getPosTags() {
        return posTags;
    }
    
    /**
     * 设置词性标注结果
     *
     * @param posTags 词性标注结果
     */
    public void setPosTags(List<PosTag> posTags) {
        this.posTags = posTags;
    }
    
    /**
     * 获取实体标注结果
     *
     * @return 实体标注结果
     */
    public List<EntityTag> getEntityTags() {
        return entityTags;
    }
    
    /**
     * 设置实体标注结果
     *
     * @param entityTags 实体标注结果
     */
    public void setEntityTags(List<EntityTag> entityTags) {
        this.entityTags = entityTags;
    }
    
    /**
     * 获取纠正建议
     *
     * @return 纠正建议
     */
    public List<CorrectionSuggestion> getCorrectionSuggestions() {
        return correctionSuggestions;
    }
    
    /**
     * 设置纠正建议
     *
     * @param correctionSuggestions 纠正建议
     */
    public void setCorrectionSuggestions(List<CorrectionSuggestion> correctionSuggestions) {
        this.correctionSuggestions = correctionSuggestions;
    }
    
    /**
     * 获取语言
     *
     * @return 语言
     */
    public String getLanguage() {
        return language;
    }
    
    /**
     * 设置语言
     *
     * @param language 语言
     */
    public void setLanguage(String language) {
        this.language = language;
    }
    
    /**
     * 添加分词结果
     *
     * @param token 分词结果
     */
    public void addToken(String token) {
        this.tokens.add(token);
    }
    
    /**
     * 添加词性标注结果
     *
     * @param posTag 词性标注结果
     */
    public void addPosTag(PosTag posTag) {
        this.posTags.add(posTag);
    }
    
    /**
     * 添加实体标注结果
     *
     * @param entityTag 实体标注结果
     */
    public void addEntityTag(EntityTag entityTag) {
        this.entityTags.add(entityTag);
    }
    
    /**
     * 添加纠正建议
     *
     * @param correctionSuggestion 纠正建议
     */
    public void addCorrectionSuggestion(CorrectionSuggestion correctionSuggestion) {
        this.correctionSuggestions.add(correctionSuggestion);
    }
    
    /**
     * 获取指定类型的实体
     *
     * @param type 实体类型
     * @return 实体列表
     */
    public List<EntityTag> getEntitiesByType(EntityType type) {
        List<EntityTag> result = new ArrayList<>();
        for (EntityTag entityTag : entityTags) {
            if (entityTag.getType() == type) {
                result.add(entityTag);
            }
        }
        return result;
    }
    
    /**
     * 获取表名实体
     *
     * @return 表名实体列表
     */
    public List<EntityTag> getTableEntities() {
        return getEntitiesByType(EntityType.TABLE);
    }
    
    /**
     * 获取列名实体
     *
     * @return 列名实体列表
     */
    public List<EntityTag> getColumnEntities() {
        return getEntitiesByType(EntityType.COLUMN);
    }
    
    /**
     * 获取值实体
     *
     * @return 值实体列表
     */
    public List<EntityTag> getValueEntities() {
        return getEntitiesByType(EntityType.VALUE);
    }
    
    /**
     * 获取函数实体
     *
     * @return 函数实体列表
     */
    public List<EntityTag> getFunctionEntities() {
        return getEntitiesByType(EntityType.FUNCTION);
    }
    
    /**
     * 获取条件实体
     *
     * @return 条件实体列表
     */
    public List<EntityTag> getConditionEntities() {
        return getEntitiesByType(EntityType.CONDITION);
    }
    
    /**
     * 获取排序实体
     *
     * @return 排序实体列表
     */
    public List<EntityTag> getOrderEntities() {
        return getEntitiesByType(EntityType.ORDER);
    }
    
    /**
     * 获取限制实体
     *
     * @return 限制实体列表
     */
    public List<EntityTag> getLimitEntities() {
        return getEntitiesByType(EntityType.LIMIT);
    }
    
    /**
     * 获取分组实体
     *
     * @return 分组实体列表
     */
    public List<EntityTag> getGroupEntities() {
        return getEntitiesByType(EntityType.GROUP);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("原始文本: ").append(originalText).append("\n");
        sb.append("标准化文本: ").append(normalizedText).append("\n");
        sb.append("语言: ").append(language).append("\n");
        sb.append("分词结果: ").append(tokens).append("\n");
        sb.append("词性标注: ").append(posTags).append("\n");
        sb.append("实体标注: ").append(entityTags).append("\n");
        sb.append("纠正建议: ").append(correctionSuggestions).append("\n");
        return sb.toString();
    }
}
