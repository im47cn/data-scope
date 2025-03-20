package com.insightdata.domain.nlquery.preprocess;

/**
 * Parts of speech in natural language
 */
public enum PartOfSpeech {
    NOUN,           // 名词
    VERB,           // 动词
    ADJECTIVE,      // 形容词
    ADVERB,         // 副词
    PRONOUN,        // 代词
    PREPOSITION,    // 介词
    CONJUNCTION,    // 连词
    INTERJECTION,   // 感叹词
    ARTICLE,        // 冠词
    NUMERAL,        // 数词
    MEASURE,        // 量词
    PARTICLE,       // 助词
    PUNCTUATION,    // 标点符号
    UNKNOWN;        // 未知词性
}