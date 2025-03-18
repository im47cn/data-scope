package com.insightdata.domain.nlquery.preprocess.tokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 默认分词器
 * 实现基本的分词功能
 */
public class DefaultTokenizer implements Tokenizer {
    
    // 分词正则表达式
    private static final Pattern TOKEN_PATTERN = Pattern.compile("\\s+");
    
    // 语言检测器
    private final LanguageDetector languageDetector;
    
    /**
     * 构造函数
     */
    public DefaultTokenizer() {
        this.languageDetector = new SimpleLanguageDetector();
    }
    
    /**
     * 构造函数
     *
     * @param languageDetector 语言检测器
     */
    public DefaultTokenizer(LanguageDetector languageDetector) {
        this.languageDetector = languageDetector;
    }
    
    @Override
    public List<String> tokenize(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 检测语言
        String language = languageDetector.detectLanguage(text);
        
        return tokenize(text, language);
    }
    
    @Override
    public List<String> tokenize(String text, String language) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 根据语言选择分词方法
        if ("zh".equals(language) || "zh-cn".equals(language) || "zh-tw".equals(language)) {
            return tokenizeChinese(text);
        } else {
            return tokenizeEnglish(text);
        }
    }
    
    /**
     * 英文分词
     *
     * @param text 输入文本
     * @return 分词结果
     */
    protected List<String> tokenizeEnglish(String text) {
        // 使用空格分割
        String[] tokens = TOKEN_PATTERN.split(text.trim());
        return Arrays.asList(tokens);
    }
    
    /**
     * 中文分词
     *
     * @param text 输入文本
     * @return 分词结果
     */
    protected List<String> tokenizeChinese(String text) {
        // 简单的字符级分词
        List<String> tokens = new ArrayList<>();
        for (char c : text.toCharArray()) {
            if (!Character.isWhitespace(c)) {
                tokens.add(String.valueOf(c));
            }
        }
        return tokens;
    }

}
