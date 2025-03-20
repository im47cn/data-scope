package com.insightdata.domain.nlquery.preprocess;

import lombok.Builder;
import lombok.Data;

/**
 * Grammatical features for NLP preprocessing
 */
@Data
@Builder
public class GrammaticalFeature {
    
    private Mood mood;        // 语气
    private GrammaticalAspect aspect;    // 体
    private Voice voice;      // 语态
    private GrammaticalPerson person;    // 人称
    private GrammaticalNumber number;    // 数
    private GrammaticalGender gender;    // 性
    private GrammaticalCase caseType;    // 格
    private Tense tense;      // 时态
    private GrammaticalFunction function; // 句法功能
    private DependencyRelation dependency; // 依存关系
    private PartOfSpeech pos;            // 词性
    
    /**
     * Create default grammatical feature
     */
    public static GrammaticalFeature createDefault() {
        return GrammaticalFeature.builder()
                .mood(Mood.UNKNOWN)
                .aspect(GrammaticalAspect.UNKNOWN)
                .voice(Voice.UNKNOWN)
                .person(GrammaticalPerson.UNKNOWN)
                .number(GrammaticalNumber.UNKNOWN)
                .gender(GrammaticalGender.UNKNOWN)
                .caseType(GrammaticalCase.UNKNOWN)
                .tense(Tense.UNKNOWN)
                .function(GrammaticalFunction.UNKNOWN)
                .dependency(DependencyRelation.UNKNOWN)
                .pos(PartOfSpeech.UNKNOWN)
                .build();
    }
}