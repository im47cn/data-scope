package com.insightdata.nlquery.intent;

/**
 * 限制类型枚举
 */
public enum LimitType {
    /**
     * 前N条
     */
    TOP_N("前N条"),
    
    /**
     * 后N条
     */
    BOTTOM_N("后N条"),
    
    /**
     * 分页
     */
    PAGINATION("分页"),
    
    /**
     * 随机N条
     */
    RANDOM_N("随机N条"),
    
    /**
     * 百分比
     */
    PERCENTAGE("百分比"),
    
    /**
     * 无限制
     */
    NONE("无限制");
    
    private final String displayName;
    
    LimitType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * 是否需要数量值
     */
    public boolean needsCount() {
        return this != NONE;
    }
    
    /**
     * 是否需要偏移量
     */
    public boolean needsOffset() {
        return this == PAGINATION;
    }
    
    /**
     * 是否需要百分比值
     */
    public boolean needsPercentage() {
        return this == PERCENTAGE;
    }
    
    /**
     * 是否需要排序
     */
    public boolean needsSorting() {
        return this == TOP_N || this == BOTTOM_N;
    }
    
    /**
     * 是否需要随机数种子
     */
    public boolean needsRandomSeed() {
        return this == RANDOM_N;
    }
    
    /**
     * 获取默认数量
     */
    public int getDefaultCount() {
        switch (this) {
            case TOP_N:
            case BOTTOM_N:
            case RANDOM_N:
                return 10;
            case PAGINATION:
                return 20;
            case PERCENTAGE:
                return 100;
            default:
                return 0;
        }
    }
    
    /**
     * 获取默认偏移量
     */
    public int getDefaultOffset() {
        return this == PAGINATION ? 0 : -1;
    }
    
    /**
     * 获取默认百分比
     */
    public double getDefaultPercentage() {
        return this == PERCENTAGE ? 100.0 : -1.0;
    }
}