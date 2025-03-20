package com.insightdata.domain.nlquery.preprocess;

/**
 * Types of phrases in natural language
 */
public enum PhraseType {
    NOUN_PHRASE,         // 名词短语
    VERB_PHRASE,         // 动词短语
    ADJECTIVE_PHRASE,    // 形容词短语
    ADVERB_PHRASE,       // 副词短语
    PREPOSITIONAL_PHRASE,// 介词短语
    CLAUSE,              // 从句
    UNKNOWN;             // 未知类型
}