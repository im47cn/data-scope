package com.insightdata.domain.nlquery.preprocess;

/**
 * 纠正级别
 */
public enum CorrectionLevel {
    /**
     * 错误
     */
    ERROR,
    
    /**
     * 警告
     */
    WARNING,
    
    /**
     * 建议
     */
    SUGGESTION,
    
    /**
     * 信息
     */
    INFO
}