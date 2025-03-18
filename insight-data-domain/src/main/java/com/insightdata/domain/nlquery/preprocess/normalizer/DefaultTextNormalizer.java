package com.insightdata.domain.nlquery.preprocess.normalizer;

import java.text.Normalizer;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 默认文本标准化实现
 */
public class DefaultTextNormalizer implements TextNormalizer {

    private NormalizerConfig config;

    private static final Pattern EMOJI_PATTERN = Pattern.compile("[\\x{1F600}-\\x{1F64F}]|[\\x{1F300}-\\x{1F5FF}]|[\\x{1F680}-\\x{1F6FF}]|[\\x{1F1E0}-\\x{1F1FF}]|[\\x{2600}-\\x{26FF}]|[\\x{2700}-\\x{27BF}]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
    private static final Pattern SPECIAL_CHARS_PATTERN = Pattern.compile("[^\\p{L}\\p{N}\\p{P}\\s]");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+([,.]\\d+)?");
    private static final Pattern PUNCTUATION_PATTERN = Pattern.compile("[\\p{P}]");
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");

    public DefaultTextNormalizer() {
        this.config = NormalizerConfig.getDefault();
    }

    public DefaultTextNormalizer(NormalizerConfig config) {
        this.config = config != null ? config : NormalizerConfig.getDefault();
    }

    @Override
    public NormalizerConfig getConfig() {
        return config;
    }

    @Override
    public void setConfig(NormalizerConfig config) {
        this.config = config != null ? config : NormalizerConfig.getDefault();
    }

    @Override
    public String normalize(String text) {
        if (text == null || text.trim().isEmpty()) {
            return text;
        }

        String normalized = text;

        // 转换为小写
        if (config.isToLowerCase()) {
            normalized = normalized.toLowerCase();
        }

        // 移除表情符号
        if (config.isRemoveEmoji()) {
            normalized = EMOJI_PATTERN.matcher(normalized).replaceAll("");
        }

        // 标准化特殊字符
        if (config.isNormalizeSpecialChars()) {
            normalized = Normalizer.normalize(normalized, Normalizer.Form.NFKC);
            normalized = SPECIAL_CHARS_PATTERN.matcher(normalized).replaceAll("");
        }

        // 标准化数字
        if (config.isNormalizeNumbers()) {
            normalized = NUMBER_PATTERN.matcher(normalized).replaceAll("#");
        }

        // 移除标点符号
        if (config.isRemovePunctuation()) {
            normalized = PUNCTUATION_PATTERN.matcher(normalized).replaceAll("");
        }

        // 移除多余空格
        if (config.isRemoveExtraWhitespace()) {
            normalized = WHITESPACE_PATTERN.matcher(normalized).replaceAll(" ").trim();
        }

        // 处理换行符
        if (!config.isPreserveLineBreaks()) {
            normalized = normalized.replaceAll("\\r\\n|\\r|\\n", " ");
        }

        // 应用自定义替换规则
        Map<String, String> replacements = config.getCustomReplacements();
        if (replacements != null) {
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                normalized = normalized.replaceAll(entry.getKey(), entry.getValue());
            }
        }

        return normalized;
    }
}
