package com.insightdata.domain.nlquery.preprocess;

/**
 * Grammatical functions in natural language
 */
public enum GrammaticalFunction {
    SUBJECT,            // 主语
    PREDICATE,          // 谓语
    OBJECT,             // 宾语
    INDIRECT_OBJECT,    // 间接宾语
    COMPLEMENT,         // 补语
    MODIFIER,           // 修饰语
    DETERMINER,         // 限定语
    ATTRIBUTE,          // 定语
    ADVERBIAL,          // 状语
    APPOSITIVE,         // 同位语
    UNKNOWN;            // 未知功能
}