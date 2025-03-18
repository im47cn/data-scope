package com.insightdata.domain.query.enums;

/**
 * 数据绑定类型枚举
 * 定义低代码平台中支持的数据绑定类型
 */
public enum BindingType {
    
    /**
     * 静态绑定
     * 直接绑定到固定的值
     */
    STATIC("静态绑定"),
    
    /**
     * 表达式绑定
     * 使用表达式计算绑定的值
     */
    EXPRESSION("表达式绑定"),
    
    /**
     * 查询绑定
     * 绑定到查询结果
     */
    QUERY("查询绑定"),
    
    /**
     * 模型绑定
     * 绑定到数据模型的某个字段
     */
    MODEL("模型绑定"),
    
    /**
     * API绑定
     * 绑定到API返回的数据
     */
    API("API绑定"),
    
    /**
     * 上下文绑定
     * 绑定到应用上下文中的数据
     */
    CONTEXT("上下文绑定"),
    
    /**
     * 事件绑定
     * 绑定到组件或应用事件
     */
    EVENT("事件绑定");
    
    private final String description;
    
    BindingType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return this.name() + "(" + this.description + ")";
    }
}