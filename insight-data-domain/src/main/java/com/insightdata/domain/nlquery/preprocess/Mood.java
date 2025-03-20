package com.insightdata.domain.nlquery.preprocess;

/**
 * Grammatical mood
 */
public enum Mood {
    INDICATIVE,    // 陈述语气
    SUBJUNCTIVE,   // 虚拟语气
    IMPERATIVE,    // 祈使语气
    CONDITIONAL,   // 条件语气
    INTERROGATIVE, // 疑问语气
    OPTATIVE,      // 祝愿语气
    POTENTIAL,     // 可能语气
    JUSSIVE,       // 命令语气
    UNKNOWN;       // 未知语气
}