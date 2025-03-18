package com.insightdata.domain.nlquery.preprocess.normalizer;

import lombok.Builder;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * 文本标准化配置
 */
@Data
@Builder
public class NormalizerConfig {

    /**
     * 是否转换为小写
     */
    @Builder.Default
    private boolean toLowerCase = false;

    /**
     * 是否移除表情符号
     */
    @Builder.Default
    private boolean removeEmoji = true;

    /**
     * 是否标准化特殊字符
     */
    @Builder.Default
    private boolean normalizeSpecialChars = true;

    /**
     * 是否标准化数字
     */
    @Builder.Default
    private boolean normalizeNumbers = false;

    /**
     * 是否移除标点符号
     */
    @Builder.Default
    private boolean removePunctuation = false;

    /**
     * 是否移除多余空格
     */
    @Builder.Default
    private boolean removeExtraWhitespace = true;

    /**
     * 是否保留换行符
     */
    @Builder.Default
    private boolean preserveLineBreaks = false;

    /**
     * 自定义替换规则
     */
    @Builder.Default
    private Map<String, String> customReplacements = new HashMap<>();

    /**
     * 获取默认配置
     */
    public static NormalizerConfig getDefault() {
        return builder()
                .toLowerCase(false)
                .removeEmoji(true)
                .normalizeSpecialChars(true)
                .normalizeNumbers(false)
                .removePunctuation(false)
                .removeExtraWhitespace(true)
                .preserveLineBreaks(false)
                .customReplacements(new HashMap<>())
                .build();
    }

    /**
     * 获取搜索优化配置
     */
    public static NormalizerConfig getSearchOptimized() {
        return builder()
                .toLowerCase(true)
                .removeEmoji(true)
                .normalizeSpecialChars(true)
                .normalizeNumbers(true)
                .removePunctuation(true)
                .removeExtraWhitespace(true)
                .preserveLineBreaks(false)
                .customReplacements(new HashMap<>())
                .build();
    }

    /**
     * 获取NL查询优化配置
     */
    public static NormalizerConfig getNLQueryOptimized() {
        return builder()
                .toLowerCase(true)
                .removeEmoji(true)
                .normalizeSpecialChars(true)
                .normalizeNumbers(false)
                .removePunctuation(false)
                .removeExtraWhitespace(true)
                .preserveLineBreaks(false)
                .customReplacements(new HashMap<>())
                .build();
    }

    /**
     * 获取精确匹配配置
     */
    public static NormalizerConfig getExactMatch() {
        return builder()
                .toLowerCase(false)
                .removeEmoji(false)
                .normalizeSpecialChars(false)
                .normalizeNumbers(false)
                .removePunctuation(false)
                .removeExtraWhitespace(false)
                .preserveLineBreaks(true)
                .customReplacements(new HashMap<>())
                .build();
    }

    /**
     * 添加自定义替换规则
     */
    public NormalizerConfig addCustomReplacement(String pattern, String replacement) {
        if (this.customReplacements == null) {
            this.customReplacements = new HashMap<>();
        }
        this.customReplacements.put(pattern, replacement);
        return this;
    }

    /**
     * 检查是否需要转换为小写
     */
    public boolean isToLowerCase() {
        return toLowerCase;
    }

    /**
     * 检查是否需要移除表情符号
     */
    public boolean isRemoveEmoji() {
        return removeEmoji;
    }

    /**
     * 检查是否需要标准化特殊字符
     */
    public boolean isNormalizeSpecialChars() {
        return normalizeSpecialChars;
    }

    /**
     * 检查是否需要标准化数字
     */
    public boolean isNormalizeNumbers() {
        return normalizeNumbers;
    }

    /**
     * 检查是否需要移除标点符号
     */
    public boolean isRemovePunctuation() {
        return removePunctuation;
    }

    /**
     * 检查是否需要移除多余空格
     */
    public boolean isRemoveExtraWhitespace() {
        return removeExtraWhitespace;
    }

    /**
     * 检查是否需要保留换行符
     */
    public boolean isPreserveLineBreaks() {
        return preserveLineBreaks;
    }

    /**
     * 获取自定义替换规则
     */
    public Map<String, String> getCustomReplacements() {
        return customReplacements == null ? new HashMap<>() : customReplacements;
    }
}