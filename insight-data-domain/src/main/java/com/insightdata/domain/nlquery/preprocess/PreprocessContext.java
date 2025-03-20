package com.insightdata.domain.nlquery.preprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.insightdata.domain.metadata.model.SchemaInfo;

/**
 * Context for text preprocessing
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreprocessContext {

    /**
     * Data source ID
     */
    private Long dataSourceId;

    /**
     * User ID
     */
    private Long userId;

    /**
     * Session ID
     */
    private String sessionId;

    /**
     * Domain
     */
    private String domain;

    /**
     * Schema metadata
     */
    private SchemaInfo metadata;

    /**
     * Language
     */
    private String language;

    /**
     * Enable spelling correction
     */
    private boolean enableSpellingCorrection;

    /**
     * Enable stop word removal
     */
    private boolean enableStopWordRemoval;

    /**
     * Enable lemmatization
     */
    private boolean enableLemmatization;

    /**
     * Enable stemming
     */
    private boolean enableStemming;

    /**
     * Enable named entity recognition
     */
    private boolean enableNER;

    /**
     * Enable part of speech tagging
     */
    private boolean enablePOSTagging;

    /**
     * Enable dependency parsing
     */
    private boolean enableDependencyParsing;
}
