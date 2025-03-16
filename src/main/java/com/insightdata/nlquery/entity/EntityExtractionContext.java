package com.insightdata.nlquery.entity;

import com.insightdata.domain.model.metadata.SchemaInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实体提取上下文
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityExtractionContext {
    
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 元数据信息
     */
    private SchemaInfo metadata;
    
    /**
     * 已提取的实体
     */
    @Builder.Default
    private List<EntityTag> extractedEntities = new ArrayList<>();
    
    /**
     * 上下文参数
     */
    @Builder.Default
    private Map<String, Object> parameters = new HashMap<>();
    
    /**
     * 是否使用模糊匹配
     */
    @Builder.Default
    private boolean useFuzzyMatching = true;
    
    /**
     * 最小置信度
     */
    @Builder.Default
    private double minConfidence = 0.6;
    
    /**
     * 是否使用元数据
     */
    @Builder.Default
    private boolean useMetadata = true;
    
    /**
     * 添加实体
     */
    public EntityExtractionContext addEntity(EntityTag entity) {
        extractedEntities.add(entity);
        return this;
    }
    
    /**
     * 添加参数
     */
    public EntityExtractionContext addParameter(String key, Object value) {
        parameters.put(key, value);
        return this;
    }
    
    /**
     * 获取参数
     */
    public Object getParameter(String key) {
        return parameters.get(key);
    }
    
    /**
     * 获取参数，带默认值
     */
    public Object getParameter(String key, Object defaultValue) {
        return parameters.getOrDefault(key, defaultValue);
    }
    
    /**
     * 是否包含参数
     */
    public boolean hasParameter(String key) {
        return parameters.containsKey(key);
    }
    
    /**
     * 移除参数
     */
    public EntityExtractionContext removeParameter(String key) {
        parameters.remove(key);
        return this;
    }
    
    /**
     * 清空参数
     */
    public EntityExtractionContext clearParameters() {
        parameters.clear();
        return this;
    }
    
    /**
     * 清空实体
     */
    public EntityExtractionContext clearEntities() {
        extractedEntities.clear();
        return this;
    }
    
    /**
     * 获取指定类型的实体
     */
    public List<EntityTag> getEntitiesByType(EntityType type) {
        List<EntityTag> entities = new ArrayList<>();
        for (EntityTag entity : extractedEntities) {
            if (entity.getType() == type) {
                entities.add(entity);
            }
        }
        return entities;
    }
    
    /**
     * 获取指定类型和置信度的实体
     */
    public List<EntityTag> getEntitiesByTypeAndConfidence(EntityType type, double minConfidence) {
        List<EntityTag> entities = new ArrayList<>();
        for (EntityTag entity : extractedEntities) {
            if (entity.getType() == type && entity.getConfidence() >= minConfidence) {
                entities.add(entity);
            }
        }
        return entities;
    }
}