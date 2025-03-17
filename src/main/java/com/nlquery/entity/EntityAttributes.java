package com.nlquery.entity;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityAttributes {
    
    /**
     * 实体文本
     */
    private String text;
    
    /**
     * 在原文中的起始位置
     */
    private int startOffset;
    
    /**
     * 在原文中的结束位置
     */
    private int endOffset;
    
    /**
     * 词性标注
     */
    private String posTag;
    
    /**
    
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