package com.insightdata.nlquery.intent;

/**
 * 查询目的
 */
public enum QueryPurpose {
    /**
     * 数据检索
     */
    DATA_RETRIEVAL,
    
    /**
     * 统计分析
     */
    STATISTICAL_ANALYSIS,
    
    /**
     * 趋势分析
     */
    TREND_ANALYSIS,
    
    /**
     * 比较分析
     */
    COMPARATIVE_ANALYSIS,
    
    /**
     * 异常检测
     */
    ANOMALY_DETECTION,
    
    /**
     * 未知目的
     */
    UNKNOWN
}
