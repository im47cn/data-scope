package com.insightdata.domain.nlquery.preprocess;

/**
 * Grammatical aspect
 */
public enum Aspect {
    SIMPLE,        // 简单体
    PROGRESSIVE,   // 进行体
    PERFECT,       // 完成体
    PERFECT_PROGRESSIVE, // 完成进行体
    HABITUAL,      // 习惯体
    ITERATIVE,     // 重复体
    MOMENTARY,     // 瞬时体
    CONTINUOUS,    // 持续体
    UNKNOWN;       // 未知体
}