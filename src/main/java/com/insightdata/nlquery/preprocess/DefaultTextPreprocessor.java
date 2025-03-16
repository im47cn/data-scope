package com.insightdata.nlquery.preprocess;

import com.insightdata.nlquery.preprocess.normalizer.TextNormalizer;
import com.insightdata.nlquery.preprocess.normalizer.TextNormalizerFactory;
import com.insightdata.nlquery.preprocess.tokenizer.LanguageDetector;
import com.insightdata.nlquery.preprocess.tokenizer.SimpleLanguageDetector;
import com.insightdata.nlquery.preprocess.tokenizer.Tokenizer;
import com.insightdata.nlquery.preprocess.tokenizer.TokenizerFactory;

import java.util.List;

/**
 * 默认文本预处理器
 * 实现基本的文本预处理功能
 */
public class DefaultTextPreprocessor implements TextPreprocessor {
    
    // 语言检测器
    private final LanguageDetector languageDetector;
    
    /**
     * 构造函数
     */
    public DefaultTextPreprocessor() {
        this.languageDetector = new SimpleLanguageDetector();
    }
    
    /**
     * 构造函数
     *
     * @param languageDetector 语言检测器
     */
    public DefaultTextPreprocessor(LanguageDetector languageDetector) {
        this.languageDetector = languageDetector;
    }
    
    @Override
    public PreprocessedText preprocess(String text) {
        return preprocess(text, new PreprocessContext());
    }
    
    @Override
    public PreprocessedText preprocess(String text, PreprocessContext context) {
        if (text == null || text.isEmpty()) {
            return new PreprocessedText("", "");
        }
        
        // 1. 检测语言
        String language = languageDetector.detectLanguage(text);
        
        // 2. 创建文本标准化器
        TextNormalizer normalizer = TextNormalizerFactory.createNormalizer(language);
        
        // 3. 标准化文本
        String normalizedText = normalizer.normalize(text);
        
        // 4. 创建分词器
        Tokenizer tokenizer = TokenizerFactory.createTokenizer(language);
        
        // 5. 分词
        List<String> tokens = tokenizer.tokenize(normalizedText, language);
        
        // 6. 创建结果
        PreprocessedText result = new PreprocessedText();
        result.setOriginalText(text);
        result.setNormalizedText(normalizedText);
        result.setTokens(tokens);
        result.setLanguage(language);
        
        return result;
    }
}
