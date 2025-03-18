package com.insightdata.domain.nlquery.preprocess.normalizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQL查询文本标准化器
 * 专门用于处理SQL查询相关的文本
 */
public class SqlQueryTextNormalizer extends DefaultTextNormalizer {
    
    // SQL关键字正则表达式
    private static final Pattern SQL_KEYWORD_PATTERN = Pattern.compile(
            "\\b(select|from|where|group|by|having|order|limit|join|on|and|or|not|in|between|like|is|null|as)\\b",
            Pattern.CASE_INSENSITIVE);
    
    // SQL函数正则表达式
    private static final Pattern SQL_FUNCTION_PATTERN = Pattern.compile(
            "\\b(count|sum|avg|min|max|concat|substring|date|now|year|month|day)\\b",
            Pattern.CASE_INSENSITIVE);
    
    @Override
    public String normalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        // 1. 基本标准化
        String result = super.normalize(text);
        
        // 2. 保留SQL关键字
        result = preserveSqlKeywords(result);
        
        // 3. 保留SQL函数
        result = preserveSqlFunctions(result);
        
        return result;
    }
    
    /**
     * 保留SQL关键字
     *
     * @param text 输入文本
     * @return 处理后的文本
     */
    protected String preserveSqlKeywords(String text) {
        Matcher matcher = SQL_KEYWORD_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group().toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
    
    /**
     * 保留SQL函数
     *
     * @param text 输入文本
     * @return 处理后的文本
     */
    protected String preserveSqlFunctions(String text) {
        Matcher matcher = SQL_FUNCTION_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group().toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
    
    @Override
    protected String handlePunctuation(String text) {
        // 保留SQL中常用的标点符号
        return text.replace(",", " , ")
                .replace(".", " . ")
                .replace("(", " ( ")
                .replace(")", " ) ")
                .replace("=", " = ")
                .replace("<", " < ")
                .replace(">", " > ")
                .replace(", ", " ! ")
                .replace(";", " ; ");
    }
}
