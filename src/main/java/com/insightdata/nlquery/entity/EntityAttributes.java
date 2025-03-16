package com.insightdata.nlquery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 实体属性
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityAttributes {
    
    /**
     * 数据类型
     */
    private String dataType;
    
    /**
     * 是否可空
     */
    private Boolean nullable;
    
    /**
     * 默认值
     */
    private String defaultValue;
    
    /**
     * 是否主键
     */
    private Boolean primaryKey;
    
    /**
     * 是否唯一键
     */
    private Boolean uniqueKey;
    
    /**
     * 是否外键
     */
    private Boolean foreignKey;
    
    /**
     * 是否自增
     */
    private Boolean autoIncrement;
    
    /**
     * 长度
     */
    private Integer length;
    
    /**
     * 精度
     */
    private Integer precision;
    
    /**
     * 小数位数
     */
    private Integer scale;
    
    /**
     * 字符集
     */
    private String characterSet;
    
    /**
     * 排序规则
     */
    private String collation;
    
    /**
     * 扩展属性
     */
    @Builder.Default
    private Map<String, Object> extensions = new HashMap<>();
    
    /**
     * 添加扩展属性
     */
    public EntityAttributes addExtension(String key, Object value) {
        extensions.put(key, value);
        return this;
    }
    
    /**
     * 获取扩展属性
     */
    public Object getExtension(String key) {
        return extensions.get(key);
    }
    
    /**
     * 获取扩展属性，带默认值
     */
    public Object getExtension(String key, Object defaultValue) {
        return extensions.getOrDefault(key, defaultValue);
    }
    
    /**
     * 是否包含扩展属性
     */
    public boolean hasExtension(String key) {
        return extensions.containsKey(key);
    }
    
    /**
     * 移除扩展属性
     */
    public EntityAttributes removeExtension(String key) {
        extensions.remove(key);
        return this;
    }
    
    /**
     * 清空扩展属性
     */
    public EntityAttributes clearExtensions() {
        extensions.clear();
        return this;
    }
}