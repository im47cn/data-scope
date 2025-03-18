package com.insightdata.domain.nlquery.preprocess.normalizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 中文文本标准化器
 * 专门用于处理中文文本
 */
public class ChineseTextNormalizer extends DefaultTextNormalizer {
    
    // 中文标点符号正则表达式
    private static final Pattern CHINESE_PUNCTUATION_PATTERN = Pattern.compile("[\\p{Punct}]");
    
    // 中文空白字符正则表达式
    private static final Pattern CHINESE_WHITESPACE_PATTERN = Pattern.compile("[\\s　]+");
    
    // 全角字符正则表达式
    private static final Pattern FULL_WIDTH_PATTERN = Pattern.compile("[\\uFF01-\\uFF5E]");
    
    @Override
    public String normalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        // 1. 转换为小写
        String result = text.toLowerCase();
        
        // 2. 全角转半角
        result = convertFullWidthToHalfWidth(result);
        
        // 3. 处理中文标点符号
        result = handleChinesePunctuation(result);
        
        // 4. 处理中文空白字符
        result = handleChineseWhitespace(result);
        
        return result;
    }
    
    /**
     * 全角转半角
     *
     * @param text 输入文本
     * @return 处理后的文本
     */
    protected String convertFullWidthToHalfWidth(String text) {
        Matcher matcher = FULL_WIDTH_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            char c = matcher.group().charAt(0);
            matcher.appendReplacement(sb, String.valueOf((char) (c - 0xFEE0)));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
    
    /**
     * 处理中文标点符号
     *
     * @param text 输入文本
     * @return 处理后的文本
     */
    protected String handleChinesePunctuation(String text) {
        // 将中文标点符号替换为空格
        return CHINESE_PUNCTUATION_PATTERN.matcher(text).replaceAll(" ");
    }
    
    /**
     * 处理中文空白字符
     *
     * @param text 输入文本
     * @return 处理后的文本
     */
    protected String handleChineseWhitespace(String text) {
        // 将中文空白字符替换为单个空格
        return CHINESE_WHITESPACE_PATTERN.matcher(text).replaceAll(" ").trim();
    }
}
