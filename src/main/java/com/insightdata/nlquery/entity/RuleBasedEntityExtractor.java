package com.insightdata.nlquery.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.insightdata.nlquery.preprocess.EntityTag;
import com.insightdata.nlquery.preprocess.EntityType;
import com.insightdata.nlquery.preprocess.PreprocessedText;

/**
 * 基于规则的实体提取器
 * 使用预定义的规则和模式来提取实体
 */
@Component
public class RuleBasedEntityExtractor implements EntityExtractor {
    
    // 表名相关的正则表达式
    private static final Pattern TABLE_PATTERN = Pattern.compile("表|用户|订单|产品|客户|员工|部门|table|user|order|product|customer|employee|department", Pattern.CASE_INSENSITIVE);
    
    // 列名相关的正则表达式
    private static final Pattern COLUMN_PATTERN = Pattern.compile("名称|姓名|邮箱|电话|地址|价格|数量|日期|时间|状态|销售额|name|email|phone|address|price|quantity|date|time|status", Pattern.CASE_INSENSITIVE);
    
    // 值相关的正则表达式
    private static final Pattern VALUE_PATTERN = Pattern.compile("\\d+|'[^']+'|\"[^\"]+\"|true|false|null", Pattern.CASE_INSENSITIVE);
    
    // 函数相关的正则表达式
    private static final Pattern FUNCTION_PATTERN = Pattern.compile("计算|统计|count|sum|avg|max|min|group|concat|substring", Pattern.CASE_INSENSITIVE);
    
    // 操作符相关的正则表达式
    private static final Pattern OPERATOR_PATTERN = Pattern.compile("等于|大于|小于|不等于|包含|开始于|结束于|between|like|in|=|>|<|!=|<>|>=|<=", Pattern.CASE_INSENSITIVE);
    
    // 条件相关的正则表达式
    private static final Pattern CONDITION_PATTERN = Pattern.compile("条件|where|having|when|if|case", Pattern.CASE_INSENSITIVE);
    
    // 排序相关的正则表达式
    private static final Pattern ORDER_PATTERN = Pattern.compile("排序|降序|升序|order|sort", Pattern.CASE_INSENSITIVE);
    
    // 限制相关的正则表达式
    private static final Pattern LIMIT_PATTERN = Pattern.compile("前|限制|limit|top", Pattern.CASE_INSENSITIVE);
    
    // 分组相关的正则表达式
    private static final Pattern GROUP_PATTERN = Pattern.compile("分组|group", Pattern.CASE_INSENSITIVE);
    
    // 日期相关的正则表达式
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}|\\d{4}/\\d{2}/\\d{2}|\\d{2}-\\d{2}-\\d{4}|\\d{2}/\\d{2}/\\d{4}|\\d{4}年|\\d{4}年\\d{1,2}月|\\d{4}年\\d{1,2}月\\d{1,2}日", Pattern.CASE_INSENSITIVE);
    
    // 数字相关的正则表达式
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+\\.?\\d*", Pattern.CASE_INSENSITIVE);
    
    // 字符串相关的正则表达式
    private static final Pattern STRING_PATTERN = Pattern.compile("'[^']+'|\"[^\"]+\"", Pattern.CASE_INSENSITIVE);
    
    // 布尔值相关的正则表达式
    private static final Pattern BOOLEAN_PATTERN = Pattern.compile("true|false|是|否|yes|no", Pattern.CASE_INSENSITIVE);
    
    @Override
    public List<EntityTag> extractEntities(PreprocessedText preprocessedText) {
        return extractEntities(preprocessedText, new EntityExtractionContext());
    }
    
    @Override
    public List<EntityTag> extractEntities(PreprocessedText preprocessedText, EntityExtractionContext context) {
        List<EntityTag> entities = new ArrayList<>();
        
        String normalizedText = preprocessedText.getNormalizedText();
        List<String> tokens = preprocessedText.getTokens();
        
        // 提取表名实体
        extractTableEntities(normalizedText, tokens, entities);
        
        // 提取列名实体
        extractColumnEntities(normalizedText, tokens, entities);
        
        // 提取值实体
        extractValueEntities(normalizedText, tokens, entities);
        
        // 提取函数实体
        extractFunctionEntities(normalizedText, tokens, entities);
        
        // 提取操作符实体
        extractOperatorEntities(normalizedText, tokens, entities);
        
        // 提取条件实体
        extractConditionEntities(normalizedText, tokens, entities);
        
        // 提取排序实体
        extractOrderEntities(normalizedText, tokens, entities);
        
        // 提取限制实体
        extractLimitEntities(normalizedText, tokens, entities);
        
        // 提取分组实体
        extractGroupEntities(normalizedText, tokens, entities);
        
        // 提取日期实体
        extractDateEntities(normalizedText, tokens, entities);
        
        // 提取数字实体
        extractNumberEntities(normalizedText, tokens, entities);
        
        // 提取字符串实体
        extractStringEntities(normalizedText, tokens, entities);
        
        // 提取布尔值实体
        extractBooleanEntities(normalizedText, tokens, entities);
        
        return entities;
    }
    
    /**
     * 提取表名实体
     */
    private void extractTableEntities(String normalizedText, List<String> tokens, List<EntityTag> entities) {
        for (String token : tokens) {
            if (TABLE_PATTERN.matcher(token).find()) {
                int startPosition = normalizedText.indexOf(token);
                if (startPosition >= 0) {
                    entities.add(new EntityTag(token, EntityType.TABLE, startPosition, startPosition + token.length(), 0.8));
                }
            }
        }
    }
    
    /**
     * 提取列名实体
     */
    private void extractColumnEntities(String normalizedText, List<String> tokens, List<EntityTag> entities) {
        for (String token : tokens) {
            if (COLUMN_PATTERN.matcher(token).find()) {
                int startPosition = normalizedText.indexOf(token);
                if (startPosition >= 0) {
                    entities.add(new EntityTag(token, EntityType.COLUMN, startPosition, startPosition + token.length(), 0.8));
                }
            }
        }
    }
    
    /**
     * 提取值实体
     */
    private void extractValueEntities(String normalizedText, List<String> tokens, List<EntityTag> entities) {
        for (String token : tokens) {
            if (VALUE_PATTERN.matcher(token).find()) {
                int startPosition = normalizedText.indexOf(token);
                if (startPosition >= 0) {
                    entities.add(new EntityTag(token, EntityType.VALUE, startPosition, startPosition + token.length(), 0.9));
                }
            }
        }
    }
    
    /**
     * 提取函数实体
     */
    private void extractFunctionEntities(String normalizedText, List<String> tokens, List<EntityTag> entities) {
        for (String token : tokens) {
            if (FUNCTION_PATTERN.matcher(token).find()) {
                int startPosition = normalizedText.indexOf(token);
                if (startPosition >= 0) {
                    entities.add(new EntityTag(token, EntityType.FUNCTION, startPosition, startPosition + token.length(), 0.9));
                }
            }
        }
    }
    
    /**
     * 提取操作符实体
     */
    private void extractOperatorEntities(String normalizedText, List<String> tokens, List<EntityTag> entities) {
        for (String token : tokens) {
            if (OPERATOR_PATTERN.matcher(token).find()) {
                int startPosition = normalizedText.indexOf(token);
                if (startPosition >= 0) {
                    entities.add(new EntityTag(token, EntityType.OPERATOR, startPosition, startPosition + token.length(), 0.9));
                }
            }
        }
    }
    
    /**
     * 提取条件实体
     */
    private void extractConditionEntities(String normalizedText, List<String> tokens, List<EntityTag> entities) {
        for (String token : tokens) {
            if (CONDITION_PATTERN.matcher(token).find()) {
                int startPosition = normalizedText.indexOf(token);
                if (startPosition >= 0) {
                    entities.add(new EntityTag(token, EntityType.CONDITION, startPosition, startPosition + token.length(), 0.8));
                }
            }
        }
    }
    
    /**
     * 提取排序实体
     */
    private void extractOrderEntities(String normalizedText, List<String> tokens, List<EntityTag> entities) {
        for (String token : tokens) {
            if (ORDER_PATTERN.matcher(token).find()) {
                int startPosition = normalizedText.indexOf(token);
                if (startPosition >= 0) {
                    entities.add(new EntityTag(token, EntityType.ORDER, startPosition, startPosition + token.length(), 0.9));
                }
            }
        }
    }
    
    /**
     * 提取限制实体
     */
    private void extractLimitEntities(String normalizedText, List<String> tokens, List<EntityTag> entities) {
        for (String token : tokens) {
            if (LIMIT_PATTERN.matcher(token).find()) {
                int startPosition = normalizedText.indexOf(token);
                if (startPosition >= 0) {
                    entities.add(new EntityTag(token, EntityType.LIMIT, startPosition, startPosition + token.length(), 0.9));
                }
            }
        }
    }
    
    /**
     * 提取分组实体
     */
    private void extractGroupEntities(String normalizedText, List<String> tokens, List<EntityTag> entities) {
        for (String token : tokens) {
            if (GROUP_PATTERN.matcher(token).find()) {
                int startPosition = normalizedText.indexOf(token);
                if (startPosition >= 0) {
                    entities.add(new EntityTag(token, EntityType.GROUP, startPosition, startPosition + token.length(), 0.9));
                }
            }
        }
    }
    
    /**
     * 提取日期实体
     */
    private void extractDateEntities(String normalizedText, List<String> tokens, List<EntityTag> entities) {
        // 使用正则表达式匹配日期
        Matcher matcher = DATE_PATTERN.matcher(normalizedText);
        while (matcher.find()) {
            String dateStr = matcher.group();
            entities.add(new EntityTag(dateStr, EntityType.DATE, matcher.start(), matcher.end(), 0.9));
        }
        
        // 特殊处理分词后的日期
        // 处理形如"2023-01-01"的日期，分词后可能变成"2023", " ", "01", " ", "01"
        for (int i = 0; i < tokens.size() - 4; i++) {
            if (tokens.get(i).matches("\\d{4}") &&
                tokens.get(i+2).matches("\\d{1,2}") &&
                tokens.get(i+4).matches("\\d{1,2}")) {
                
                String dateStr = tokens.get(i) + "-" + tokens.get(i+2) + "-" + tokens.get(i+4);
                int startPosition = normalizedText.indexOf(tokens.get(i));
                if (startPosition >= 0) {
                    int endPosition = normalizedText.indexOf(tokens.get(i+4), startPosition) + tokens.get(i+4).length();
                    entities.add(new EntityTag(dateStr, EntityType.DATE, startPosition, endPosition, 0.8));
                }
            }
        }
    }
    
    /**
     * 提取数字实体
     */
    private void extractNumberEntities(String normalizedText, List<String> tokens, List<EntityTag> entities) {
        Matcher matcher = NUMBER_PATTERN.matcher(normalizedText);
        while (matcher.find()) {
            String numberStr = matcher.group();
            entities.add(new EntityTag(numberStr, EntityType.NUMBER, matcher.start(), matcher.end(), 0.9));
        }
    }
    
    /**
     * 提取字符串实体
     */
    private void extractStringEntities(String normalizedText, List<String> tokens, List<EntityTag> entities) {
        // 使用正则表达式匹配字符串
        Matcher matcher = STRING_PATTERN.matcher(normalizedText);
        while (matcher.find()) {
            String stringStr = matcher.group();
            entities.add(new EntityTag(stringStr, EntityType.STRING, matcher.start(), matcher.end(), 0.9));
        }
        
        // 特殊处理分词后的字符串
        // 处理形如"'测试'"的字符串，分词后可能变成" ", "测试", " "
        for (int i = 0; i < tokens.size() - 2; i++) {
            if (tokens.get(i).equals(" ") &&
                !tokens.get(i+1).trim().isEmpty() &&
                tokens.get(i+2).equals(" ")) {
                
                String stringStr = "'" + tokens.get(i+1) + "'";
                int startPosition = normalizedText.indexOf(tokens.get(i));
                if (startPosition >= 0) {
                    int endPosition = normalizedText.indexOf(tokens.get(i+2), startPosition) + tokens.get(i+2).length();
                    entities.add(new EntityTag(stringStr, EntityType.STRING, startPosition, endPosition, 0.8));
                }
            }
        }
    }
    
    /**
     * 提取布尔值实体
     */
    private void extractBooleanEntities(String normalizedText, List<String> tokens, List<EntityTag> entities) {
        Matcher matcher = BOOLEAN_PATTERN.matcher(normalizedText);
        while (matcher.find()) {
            String booleanStr = matcher.group();
            entities.add(new EntityTag(booleanStr, EntityType.BOOLEAN, matcher.start(), matcher.end(), 0.9));
        }
    }
}