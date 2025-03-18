package com.insightdata.domain.nlquery.preprocess.tokenizer;

import java.util.regex.Pattern;

/**
 * 简单语言检测器
 * 实现基本的语言检测功能
 */
public class SimpleLanguageDetector implements LanguageDetector {
    
    // 中文字符正则表达式
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\\u4E00-\\u9FA5]");
    
    // 日文字符正则表达式
    private static final Pattern JAPANESE_PATTERN = Pattern.compile("[\\u3040-\\u309F\\u30A0-\\u30FF]");
    
    // 韩文字符正则表达式
    private static final Pattern KOREAN_PATTERN = Pattern.compile("[\\uAC00-\\uD7A3\\u1100-\\u11FF\\u3130-\\u318F]");
    
    // 俄文字符正则表达式
    private static final Pattern RUSSIAN_PATTERN = Pattern.compile("[\\u0400-\\u04FF]");
    
    // 阿拉伯文字符正则表达式
    private static final Pattern ARABIC_PATTERN = Pattern.compile("[\\u0600-\\u06FF]");
    
    @Override
    public String detectLanguage(String text) {
        if (text == null || text.isEmpty()) {
            return "en";
        }
        
        // 计算各种语言字符的数量
        int chineseCount = countMatches(CHINESE_PATTERN, text);
        int japaneseCount = countMatches(JAPANESE_PATTERN, text);
        int koreanCount = countMatches(KOREAN_PATTERN, text);
        int russianCount = countMatches(RUSSIAN_PATTERN, text);
        int arabicCount = countMatches(ARABIC_PATTERN, text);
        
        // 根据字符数量判断语言
        if (chineseCount > 0 && chineseCount >= japaneseCount && chineseCount >= koreanCount) {
            return "zh";
        } else if (japaneseCount > 0 && japaneseCount >= chineseCount && japaneseCount >= koreanCount) {
            return "ja";
        } else if (koreanCount > 0 && koreanCount >= chineseCount && koreanCount >= japaneseCount) {
            return "ko";
        } else if (russianCount > 0) {
            return "ru";
        } else if (arabicCount > 0) {
            return "ar";
        } else {
            return "en";
        }
    }
    
    /**
     * 计算正则表达式匹配的字符数量
     *
     * @param pattern 正则表达式
     * @param text 输入文本
     * @return 匹配的字符数量
     */
    private int countMatches(Pattern pattern, String text) {
        int count = 0;
        java.util.regex.Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}
