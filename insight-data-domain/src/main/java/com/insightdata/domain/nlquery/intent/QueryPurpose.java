package com.insightdata.domain.nlquery.intent;

/**
 * 查询目的枚举
 */
public enum QueryPurpose {
    /**
     * 数据检索
     */
    DATA_RETRIEVAL("数据检索"),
    
    /**
     * 统计分析
     */
    STATISTICAL_ANALYSIS("统计分析"),
    
    /**
     * 趋势分析
     */
    TREND_ANALYSIS("趋势分析"),
    
    /**
     * 对比分析
     */
    COMPARISON_ANALYSIS("对比分析"),
    
    /**
     * 关联分析
     */
    CORRELATION_ANALYSIS("关联分析"),
    
    /**
     * 分组分析
     */
    GROUP_ANALYSIS("分组分析"),
    
    /**
     * 排名分析
     */
    RANKING_ANALYSIS("排名分析"),
    
    /**
     * 占比分析
     */
    PROPORTION_ANALYSIS("占比分析"),
    
    /**
     * 预测分析
     */
    PREDICTIVE_ANALYSIS("预测分析"),
    
    /**
     * 异常检测
     */
    ANOMALY_DETECTION("异常检测"),
    
    /**
     * 未知
     */
    UNKNOWN("未知");
    
    private final String displayName;
    
    QueryPurpose(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * 是否是统计类分析
     */
    public boolean isStatistical() {
        return this == STATISTICAL_ANALYSIS || 
               this == TREND_ANALYSIS || 
               this == COMPARISON_ANALYSIS || 
               this == CORRELATION_ANALYSIS || 
               this == GROUP_ANALYSIS || 
               this == RANKING_ANALYSIS || 
               this == PROPORTION_ANALYSIS;
    }
    
    /**
     * 是否是预测类分析
     */
    public boolean isPredictive() {
        return this == PREDICTIVE_ANALYSIS || 
               this == ANOMALY_DETECTION;
    }
    
    /**
     * 是否需要时间维度
     */
    public boolean needsTimeRange() {
        return this == TREND_ANALYSIS || 
               this == PREDICTIVE_ANALYSIS;
    }
    
    /**
     * 是否需要分组
     */
    public boolean needsGrouping() {
        return this == GROUP_ANALYSIS || 
               this == RANKING_ANALYSIS || 
               this == PROPORTION_ANALYSIS;
    }
    
    /**
     * 是否需要排序
     */
    public boolean needsSorting() {
        return this == RANKING_ANALYSIS;
    }
    
    /**
     * 是否需要限制条件
     */
    public boolean needsLimit() {
        return this == RANKING_ANALYSIS;
    }
}
