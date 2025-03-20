package com.insightdata.domain.nlquery.preprocess;

/**
 * Grammatical voice
 */
public enum Voice {
    ACTIVE,      // 主动语态
    PASSIVE,     // 被动语态
    MIDDLE,      // 中间语态
    REFLEXIVE,   // 反身语态
    RECIPROCAL,  // 互动语态
    CAUSATIVE,   // 使役语态
    UNKNOWN;     // 未知语态
}