package com.insightdata.domain.nlquery.preprocess;

/**
 * Dependency relations in natural language
 */
public enum DependencyRelation {
    SUBJECT,            // 主语关系
    OBJECT,             // 宾语关系
    INDIRECT_OBJECT,    // 间接宾语关系
    MODIFIER,           // 修饰语关系
    COMPLEMENT,         // 补语关系
    COORDINATION,       // 并列关系
    SUBORDINATION,      // 从属关系
    PREPOSITIONAL,      // 介词关系
    POSSESSION,         // 所属关系
    TEMPORAL,           // 时间关系
    SPATIAL,            // 空间关系
    CAUSAL,            // 因果关系
    CONDITION,          // 条件关系
    PURPOSE,            // 目的关系
    MANNER,            // 方式关系
    UNKNOWN;           // 未知关系
}