package com.insightdata.domain.nlquery.intent;

/**
 * Query purpose enum that represents the purpose of a natural language query
 */
public enum QueryPurpose {
    DATA_RETRIEVAL,         // 数据检索
    STATISTICAL_ANALYSIS,   // 统计分析
    TREND_ANALYSIS,        // 趋势分析
    COMPARISON,            // 对比分析
    AGGREGATION,           // 聚合分析
    FILTERING,             // 数据过滤
    SORTING,               // 数据排序
    GROUPING,              // 数据分组
    RANKING,               // 排名分析
    CORRELATION,           // 相关性分析
    ANOMALY_DETECTION,     // 异常检测
    PATTERN_RECOGNITION,   // 模式识别
    FORECASTING,           // 预测分析
    UNKNOWN               // 未知目的
}
