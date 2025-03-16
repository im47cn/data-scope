package com.insightdata.nlquery.preprocess;

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
     * 是否可空
     */
    private Boolean nullable;
    
    /**
     * 是否唯一
     */
    private Boolean unique;
    
    /**
     * 是否主键
     */
    private Boolean primaryKey;
    
    /**
     * 是否外键
     */
    private Boolean foreignKey;
    
    /**
     * 是否索引
     */
    private Boolean indexed;
    
    /**
     * 是否自增
     */
    private Boolean autoIncrement;
    
    /**
     * 默认值
     */
    private String defaultValue;
    
    /**
     * 注释
     */
    private String comment;
    
    /**
     * 额外属性
     */
    @Builder.Default
    private Map<String, Object> extraAttributes = new HashMap<>();
    
    /**
     * 获取额外属性
     */
    @SuppressWarnings("unchecked")
    public <T> T getExtraAttribute(String key) {
        return (T) extraAttributes.get(key);
    }
    
    /**
     * 设置额外属性
     */
    public void setExtraAttribute(String key, Object value) {
        extraAttributes.put(key, value);
    }
}