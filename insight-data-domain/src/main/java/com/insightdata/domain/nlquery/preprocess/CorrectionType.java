package com.insightdata.domain.nlquery.preprocess;

/**
 * 纠正类型
 */
public enum CorrectionType {
    /**
     * 拼写错误
     */
    SPELLING,
    
    /**
     * 语法错误
     */
    GRAMMAR,
    
    /**
     * 标点符号错误
     */
    PUNCTUATION,
    
    /**
     * 大小写错误
     */
    CASE,
    
    /**
     * 空格错误
     */
    SPACING,
    
    /**
     * 其他错误
     */
    OTHER
}
