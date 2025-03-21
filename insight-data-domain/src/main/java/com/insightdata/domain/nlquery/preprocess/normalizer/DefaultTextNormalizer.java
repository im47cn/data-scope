package com.insightdata.domain.nlquery.preprocess.normalizer;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * 默认文本标准化实现
 */
public class DefaultTextNormalizer implements TextNormalizer {

    private NormalizerConfig config;

    private static final Pattern EMOJI_PATTERN = Pattern.compile("[\\x{1F300}-\\x{1F9FF}]", Pattern.UNICODE_CHARACTER_CLASS);
    private static final Pattern SPECIAL_CHARS_PATTERN = Pattern.compile("[^\\p{L}\\p{N}\\p{P}\\s]");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+(\\.\\d+)?");
    private static final Pattern PUNCTUATION_PATTERN = Pattern.compile("\\p{P}");
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
    private static final Pattern LINEBREAK_PATTERN = Pattern.compile("\\r?\\n");

    public DefaultTextNormalizer() {
        this.config = NormalizerConfig.getDefault();
    }

    public DefaultTextNormalizer(NormalizerConfig config) {
        this.config = config;
    }

    @Override
    public NormalizerConfig getConfig() {
        return this.config;
    }

    @Override
    public void setConfig(NormalizerConfig config) {
        this.config = config;
    }

    @Override
    public String normalize(String text, String language) {
        if (text == null || text.trim().isEmpty()) {
            return text;
        }

        String result = text;

        if (config.isToLowerCase()) {
            result = result.toLowerCase();
        }

        if (config.isRemoveEmoji()) {
            result = EMOJI_PATTERN.matcher(result).replaceAll("");
        }

        if (config.isNormalizeSpecialChars()) {
            result = SPECIAL_CHARS_PATTERN.matcher(result).replaceAll("");
        }

        if (config.isNormalizeNumbers()) {
            result = NUMBER_PATTERN.matcher(result).replaceAll("#");
        }

        if (config.isRemovePunctuation()) {
            result = PUNCTUATION_PATTERN.matcher(result).replaceAll("");
        }

        if (config.isRemoveExtraWhitespace()) {
            result = WHITESPACE_PATTERN.matcher(result).replaceAll(" ");
        }

        if (!config.isPreserveLineBreaks()) {
            result = LINEBREAK_PATTERN.matcher(result).replaceAll(" ");
        }

        Map<String, String> replacements = config.getCustomReplacements();
        if (replacements != null) {
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                result = result.replace(entry.getKey(), entry.getValue());
            }
        }

        return result.trim();
    }
}
