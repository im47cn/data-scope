package com.insightdata.domain.nlquery.preprocess.language;

import java.util.regex.Pattern;

/**
 * 简单语言检测器实现
 */
public class SimpleLanguageDetector implements LanguageDetector {

    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\\u4e00-\\u9fa5]");
    private static final Pattern ENGLISH_PATTERN = Pattern.compile("[a-zA-Z]");

    @Override
    public String detectLanguage(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "zh-CN"; // 默认中文
        }

        int chineseCount = countMatches(CHINESE_PATTERN, text);
        int englishCount = countMatches(ENGLISH_PATTERN, text);

        // 根据中英文字符数量判断语言
        if (chineseCount > englishCount) {
            return "zh-CN";
        } else if (englishCount > chineseCount) {
            return "en-US";
        } else {
            return "zh-CN"; // 相等时默认中文
        }
    }

    private int countMatches(Pattern pattern, String text) {
        int count = 0;
        java.util.regex.Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}