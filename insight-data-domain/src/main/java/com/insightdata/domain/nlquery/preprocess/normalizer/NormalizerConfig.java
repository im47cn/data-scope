package com.insightdata.domain.nlquery.preprocess.normalizer;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * 文本标准化配置
 */
@Data
public class NormalizerConfig {

    private boolean toLowerCase = true;
    private boolean removeEmoji = true;
    private boolean normalizeSpecialChars = true;
    private boolean normalizeNumbers = true;
    private boolean removePunctuation = false;
    private boolean removeExtraWhitespace = true;
    private boolean preserveLineBreaks = true;
    private Map<String, String> customReplacements = new HashMap<>();

    private NormalizerConfig() {}

    public static NormalizerConfigBuilder builder() {
        return new NormalizerConfigBuilder();
    }

    public static NormalizerConfig getDefault() {
        return builder().build();
    }

    public static class NormalizerConfigBuilder {
        private final NormalizerConfig config;

        private NormalizerConfigBuilder() {
            config = new NormalizerConfig();
        }

        public NormalizerConfigBuilder toLowerCase(boolean toLowerCase) {
            config.toLowerCase = toLowerCase;
            return this;
        }

        public NormalizerConfigBuilder removeEmoji(boolean removeEmoji) {
            config.removeEmoji = removeEmoji;
            return this;
        }

        public NormalizerConfigBuilder normalizeSpecialChars(boolean normalizeSpecialChars) {
            config.normalizeSpecialChars = normalizeSpecialChars;
            return this;
        }

        public NormalizerConfigBuilder normalizeNumbers(boolean normalizeNumbers) {
            config.normalizeNumbers = normalizeNumbers;
            return this;
        }

        public NormalizerConfigBuilder removePunctuation(boolean removePunctuation) {
            config.removePunctuation = removePunctuation;
            return this;
        }

        public NormalizerConfigBuilder removeExtraWhitespace(boolean removeExtraWhitespace) {
            config.removeExtraWhitespace = removeExtraWhitespace;
            return this;
        }

        public NormalizerConfigBuilder preserveLineBreaks(boolean preserveLineBreaks) {
            config.preserveLineBreaks = preserveLineBreaks;
            return this;
        }

        public NormalizerConfigBuilder customReplacements(Map<String, String> customReplacements) {
            config.customReplacements = customReplacements;
            return this;
        }

        public NormalizerConfig build() {
            return config;
        }
    }
}