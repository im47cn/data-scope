package com.insightdata.domain.nlquery.preprocess;

/**
 * Verb tenses in natural language
 */
public enum Tense {
    PRESENT,            // 现在时
    PAST,              // 过去时
    FUTURE,            // 将来时
    PRESENT_PERFECT,   // 现在完成时
    PAST_PERFECT,      // 过去完成时
    FUTURE_PERFECT,    // 将来完成时
    PRESENT_CONTINUOUS,// 现在进行时
    PAST_CONTINUOUS,   // 过去进行时
    FUTURE_CONTINUOUS, // 将来进行时
    UNKNOWN;           // 未知时态
}