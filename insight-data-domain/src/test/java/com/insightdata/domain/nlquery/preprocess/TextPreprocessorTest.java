package com.insightdata.domain.nlquery.preprocess;

/**
 * 文本预处理器测试类
 */
public class TextPreprocessorTest {
    
    /**
     * 主方法，用于测试文本预处理器
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 创建文本预处理器
        DefaultTextPreprocessor preprocessor = new DefaultTextPreprocessor();
        
        // 测试英文文本
        testEnglishText(preprocessor);
        
        // 测试中文文本
        testChineseText(preprocessor);
        
        // 测试SQL查询文本
        testSqlQueryText(preprocessor);
    }
    
    /**
     * 测试英文文本
     *
     * @param preprocessor 文本预处理器
     */
    private static void testEnglishText(DefaultTextPreprocessor preprocessor) {
        System.out.println("=== 测试英文文本 ===");
        
        String text = "Hello, world! This is a test.";
        PreprocessedText result = preprocessor.preprocess(text);
        
        System.out.println("原始文本: " + result.getOriginalText());
        System.out.println("标准化文本: " + result.getNormalizedText());
        System.out.println("分词结果: " + result.getTokens());
        System.out.println("语言: " + result.getLanguage());
        System.out.println();
    }
    
    /**
     * 测试中文文本
     *
     * @param preprocessor 文本预处理器
     */
    private static void testChineseText(DefaultTextPreprocessor preprocessor) {
        System.out.println("=== 测试中文文本 ===");
        
        String text = "你好，世界！这是一个测试。";
        PreprocessedText result = preprocessor.preprocess(text);
        
        System.out.println("原始文本: " + result.getOriginalText());
        System.out.println("标准化文本: " + result.getNormalizedText());
        System.out.println("分词结果: " + result.getTokens());
        System.out.println("语言: " + result.getLanguage());
        System.out.println();
    }
    
    /**
     * 测试SQL查询文本
     *
     * @param preprocessor 文本预处理器
     */
    private static void testSqlQueryText(DefaultTextPreprocessor preprocessor) {
        System.out.println("=== 测试SQL查询文本 ===");
        
        String text = "查询所有用户的姓名和邮箱";
        
        // 创建上下文
        PreprocessContext context = new PreprocessContext();
        context.setDataSourceId(1L);
        context.setUserId(1L);
        context.setSessionId("test-session");
        context.setDomain("user");
        
        PreprocessedText result = preprocessor.preprocess(text);
        
        System.out.println("原始文本: " + result.getOriginalText());
        System.out.println("标准化文本: " + result.getNormalizedText());
        System.out.println("分词结果: " + result.getTokens());
        System.out.println("语言: " + result.getLanguage());
        System.out.println();
    }
}
