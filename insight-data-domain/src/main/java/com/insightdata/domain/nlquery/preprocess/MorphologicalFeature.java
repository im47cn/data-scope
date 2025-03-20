package com.insightdata.domain.nlquery.preprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Morphological features for text preprocessing
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MorphologicalFeature {

    /**
     * Word root
     */
    private String root;

    /**
     * Word prefix
     */
    private String prefix;

    /**
     * Word suffix
     */
    private String suffix;

    /**
     * Word tense
     */
    private Tense tense;

    /**
     * Word number
     */
    private Number number;

    /**
     * Word gender
     */
    private Gender gender;

    /**
     * Word case
     */
    private Case wordCase;

    /**
     * Word person
     */
    private Person person;

    /**
     * Word mood
     */
    private Mood mood;

    /**
     * Word voice
     */
    private Voice voice;

    /**
     * Word aspect
     */
    private Aspect aspect;

    /**
     * Tense enum
     */
    public enum Tense {
        PRESENT,
        PAST,
        FUTURE,
        PRESENT_PERFECT,
        PAST_PERFECT,
        FUTURE_PERFECT,
        UNSPECIFIED
    }

    /**
     * Number enum
     */
    public enum Number {
        SINGULAR,
        PLURAL,
        UNSPECIFIED
    }

    /**
     * Gender enum
     */
    public enum Gender {
        MASCULINE,
        FEMININE,
        NEUTER,
        UNSPECIFIED
    }

    /**
     * Case enum
     */
    public enum Case {
        NOMINATIVE,
        ACCUSATIVE,
        DATIVE,
        GENITIVE,
        UNSPECIFIED
    }

    /**
     * Person enum
     */
    public enum Person {
        FIRST,
        SECOND,
        THIRD,
        UNSPECIFIED
    }

    /**
     * Mood enum
     */
    public enum Mood {
        INDICATIVE,
        SUBJUNCTIVE,
        IMPERATIVE,
        UNSPECIFIED
    }

    /**
     * Voice enum
     */
    public enum Voice {
        ACTIVE,
        PASSIVE,
        MIDDLE,
        UNSPECIFIED
    }

    /**
     * Aspect enum
     */
    public enum Aspect {
        PERFECTIVE,
        IMPERFECTIVE,
        PROGRESSIVE,
        UNSPECIFIED
    }

    /**
     * Create a new morphological feature
     */
    public static MorphologicalFeature of(String root) {
        return MorphologicalFeature.builder()
                .root(root)
                .build();
    }

    /**
     * Create a new morphological feature with prefix and suffix
     */
    public static MorphologicalFeature of(String root, String prefix, String suffix) {
        return MorphologicalFeature.builder()
                .root(root)
                .prefix(prefix)
                .suffix(suffix)
                .build();
    }

    /**
     * Create a new morphological feature with all fields
     */
    public static MorphologicalFeature of(String root, String prefix, String suffix,
            Tense tense, Number number, Gender gender, Case wordCase,
            Person person, Mood mood, Voice voice, Aspect aspect) {
        return MorphologicalFeature.builder()
                .root(root)
                .prefix(prefix)
                .suffix(suffix)
                .tense(tense)
                .number(number)
                .gender(gender)
                .wordCase(wordCase)
                .person(person)
                .mood(mood)
                .voice(voice)
                .aspect(aspect)
                .build();
    }
}