package com.nlquery.preprocess;

import com.nlquery.entity.EntityTag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 默认文本预处理器实现
 */
@Slf4j
@Component
public class DefaultTextPreprocessor implements TextPreprocessor {
    
    private static final String[] SUPPORTED_LANGUAGES = {"zh-CN", "en-US"};
    
    @Override
    public PreprocessedText preprocess(String text) {
        try {
            if (text == null || text.trim().isEmpty()) {
                return PreprocessedText.builder()
                        .success(false)
                        .errorMessage("输入文本为空")
                        .build();
            }
            
            // 标准化文本
            String normalizedText = normalizeText(text);
            
            // 分词
            List<String> tokens = tokenize(normalizedText);
            
            // 词性标注
            List<PosTag> posTags = posTag(tokens);
            
            // 命名实体识别
            List<EntityTag> entities = extractEntities(tokens, posTags);
            
            // 依存句法分析
            List<String> dependencies = parseDependencies(tokens, posTags);
            
            return PreprocessedText.builder()
                    .originalText(text)
                    .normalizedText(normalizedText)
                    .tokens(tokens)
                    .posTags(posTags)
                    .language(detectLanguage(text))
                    .success(true)
                    .build();
                    
        } catch (Exception e) {
            log.error("文本预处理失败", e);
            return PreprocessedText.builder()
                    .originalText(text)
                    .success(false)
                    .errorMessage(e.getMessage())
                    .build();
        }
    }
    
    @Override
    public PreprocessedText preprocess(String text, PreprocessContext context) {
        PreprocessedText result = preprocess(text);
        if (result.isSuccess() && context != null) {
            // 使用上下文信息增强预处理结果
            enhanceWithContext(result, context);
        }
        return result;
    }
    
    @Override
    public String[] getSupportedLanguages() {
        return SUPPORTED_LANGUAGES;
    }
    
    /**
     * 标准化文本
     */
    protected String normalizeText(String text) {
        if (text == null) {
            return "";
        }
        // 1. 转换为小写
        text = text.toLowerCase();
        // 2. 去除多余空白字符
        text = text.replaceAll("\\s+", " ").trim();
        // 3. 统一标点符号
        text = normalizePunctuation(text);
        return text;
    }
    
    /**
     * 标准化标点符号
     */
    protected String normalizePunctuation(String text) {
        // 1. 全角转半角
        text = text.replace("\u3002", ".") // 句号
                  .replace("\uff0c", ",") // 逗号
                  .replace("\uff1f", "?") // 问号
                  .replace("\uff01", "!") // 感叹号
                  .replace("\uff1a", ":") // 冒号
                  .replace("\uff1b", ";") // 分号
                  .replace("\u201c", "\"") // 左双引号
                  .replace("\u201d", "\"") // 右双引号
                  .replace("\u2018", "'") // 左单引号
                  .replace("\u2019", "'") // 右单引号
                  .replace("\uff08", "(") // 左括号
                  .replace("\uff09", ")"); // 右括号
        // 2. 规范化空格
        text = text.replaceAll("([,;:!?])", "$1 ");
        return text;
    }
    
    /**
     * 分词
     */
    protected List<String> tokenize(String text) {
        // 简单实现：按空格分词
        return new ArrayList<>(Arrays.asList(text.split("\\s+")));
    }
    
    /**
     * 词性标注
     */
    protected List<PosTag> posTag(List<String> tokens) {
        // 简单实现：返回空标注
        return new ArrayList<>(tokens.size());
    }
    
    /**
     * 命名实体识别
     */
    protected List<EntityTag> extractEntities(List<String> tokens, List<PosTag> posTags) {
        // 简单实现：返回空实体列表
        return new ArrayList<>();
    }
    
    /**
     * 依存句法分析
     */
    protected List<String> parseDependencies(List<String> tokens, List<PosTag> posTags) {
        // 简单实现：返回空依存关系
        return new ArrayList<>();
    }
    
    /**
     * 语言检测
     */
    protected String detectLanguage(String text) {
        // 简单实现：默认中文
        return "zh-CN";
    }
    
    /**
     * 使用上下文信息增强预处理结果
     */
    protected void enhanceWithContext(PreprocessedText result, PreprocessContext context) {
        // 1. 设置语言
        if (context.getLanguage() != null) {
            result.setLanguage(context.getLanguage());
        }
        
        // 2. 根据上下文参数调整实体识别结果
        if (context.getParameters() != null && !context.getParameters().isEmpty()) {
            enhanceEntities(result, context);
        }
        
        // 3. 根据上下文标签调整置信度
        if (context.getTags() != null && !context.getTags().isEmpty()) {
            adjustConfidence(result, context);
        }
    }
    
    /**
     * 增强实体识别结果
     */
    protected void enhanceEntities(PreprocessedText result, PreprocessContext context) {
        // 简单实现：不做任何增强
    }
    
    /**
     * 调整置信度
     */
    protected void adjustConfidence(PreprocessedText result, PreprocessContext context) {
        // 简单实现：不做任何调整
    }
}
