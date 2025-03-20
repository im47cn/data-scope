package com.insightdata.domain.nlquery.preprocess;

/**
 * 纠正建议的级别
 */
public enum CorrectionLevel {
    /**
     * 单词级别的纠正
     */
    WORD,

    /**
     * 句子级别的纠正
     */
    SENTENCE,

    /**
     * 上下文级别的纠正
     */
    CONTEXT,

    /**
     * 关键性纠正
     */
    CRITICAL
}