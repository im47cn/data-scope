package com.insightdata.domain.nlquery.preprocess;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.insightdata.domain.nlquery.preprocess.normalizer.TextNormalizer;
import com.insightdata.domain.nlquery.preprocess.tokenizer.Tokenizer;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * 文本预处理器测试类
 */
public class TextPreprocessorTest {
    
    private DefaultTextPreprocessor preprocessor;
    
    @Mock
    private TextNormalizer textNormalizer;
    
    @Mock
    private Tokenizer tokenizer;
    
    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        
        // Mock the normalizer behavior
        when(textNormalizer.normalize(anyString())).thenAnswer(i -> i.getArgument(0));
        
        // Mock the tokenizer behavior
        when(tokenizer.tokenize(anyString())).thenAnswer(i -> {
            String text = i.getArgument(0);
            return java.util.Arrays.asList(text.split("\\s+"));
        });
        
        preprocessor = new DefaultTextPreprocessor(textNormalizer, tokenizer);
    }
    
    @Test
    public void testEnglishText() {
        String text = "Hello, world! This is a test.";
        PreprocessedText result = preprocessor.preprocess(text);
        
        assertNotNull(result);
        assertEquals(text, result.getOriginalText());
        assertNotNull(result.getNormalizedText());
        assertNotNull(result.getTokens());
        assertEquals("en-US", result.getLanguage());
    }
    
    @Test
    public void testChineseText() {
        String text = "你好，世界！这是一个测试。";
        PreprocessedText result = preprocessor.preprocess(text);
        
        assertNotNull(result);
        assertEquals(text, result.getOriginalText());
        assertNotNull(result.getNormalizedText());
        assertNotNull(result.getTokens());
        assertEquals("zh-CN", result.getLanguage());
    }
    
    @Test
    public void testSqlQueryText() {
        String text = "查询所有用户的姓名和邮箱";
        PreprocessedText result = preprocessor.preprocess(text);
        
        assertNotNull(result);
        assertEquals(text, result.getOriginalText());
        assertNotNull(result.getNormalizedText());
        assertNotNull(result.getTokens());
        assertNotNull(result.getTokenFeatures());
        assertNotNull(result.getCorrections());
    }
}
