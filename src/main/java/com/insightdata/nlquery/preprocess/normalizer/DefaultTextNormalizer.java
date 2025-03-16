package com.insightdata.nlquery.preprocess.normalizer;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * 默认文本标准化器
 * 实现基本的文本标准化功能
 */
public class DefaultTextNormalizer implements TextNormalizer {
    
    // 标点符号正则表达式
    private static final Pattern PUNCTUATION_PATTERN = Pattern.compile("[\\p{Punct}]");
    
    // 空白字符正则表达式
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
    
    // 特殊字符正则表达式
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^\\p{L}\\p{N}\\s]");
    
    @Override
    public String normalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        // 1. 转换为小写
        String result = text.toLowerCase();
        
        // 2. Unicode标准化
        result = Normalizer.normalize(result, Normalizer.Form.NFKC);
        
        // 3. 处理标点符号
        result = handlePunctuation(result);
        
        // 4. 处理空白字符
        result = handleWhitespace(result);
        
        // 5. 处理特殊字符
        result = handleSpecialCharacters(result);
        
        return result;
    }
    
    /**
     * 处理标点符号
     *
     * @param text 输入文本
     * @return 处理后的文本
     */
    protected String handlePunctuation(String text) {
        // 将标点符号替换为空格
        return PUNCTUATION_PATTERN.matcher(text).replaceAll(" ");
    }
    
    /**
     * 处理空白字符
     *
     * @param text 输入文本
     * @return 处理后的文本
     */
    protected String handleWhitespace(String text) {
        // 将连续的空白字符替换为单个空格
        return WHITESPACE_PATTERN.matcher(text).replaceAll(" ").trim();
    }
    
    /**
     * 处理特殊字符
     *
     * @param text 输入文本
     * @return 处理后的文本
     */
    protected String handleSpecialCharacters(String text) {
        // 将特殊字符替换为空格
        return SPECIAL_CHAR_PATTERN.matcher(text).replaceAll(" ");
    }
}
