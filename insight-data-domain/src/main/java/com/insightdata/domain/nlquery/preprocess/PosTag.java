package com.insightdata.domain.nlquery.preprocess;

/**
 * 词性标注枚举
 */
public class PosTag {
    public enum TagType {
        NOUN,       // 名词
        VERB,       // 动词
        ADJECTIVE,  // 形容词
        ADVERB,     // 副词
        PRONOUN,    // 代词
        PREPOSITION,// 介词
        CONJUNCTION,// 连词
        NUMERAL,    // 数词
        OTHER       // 其他
    }
}
