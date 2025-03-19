package com.insightdata.domain.nlquery.preprocess;

import com.insightdata.domain.nlquery.preprocess.normalizer.NormalizerConfig;
import com.insightdata.domain.nlquery.preprocess.normalizer.TextNormalizer;
import com.insightdata.domain.nlquery.preprocess.tokenizer.Tokenizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 默认文本预处理器实现
 * 提供标准化、分词、词性标注、拼写检查等功能
 */
@Slf4j
@Component
public class DefaultTextPreprocessor implements TextPreprocessor {

    private TextNormalizer textNormalizer;
    private Tokenizer tokenizer;

    // 简单的拼写错误词典（实际应使用更复杂的拼写检查算法）
    private static final Map<String, String> COMMON_MISSPELLINGS = new HashMap<>();

    // 常见技术术语
    private static final List<String> TECH_TERMS = new ArrayList<>();

    static {
        // 初始化常见拼写错误词典
        COMMON_MISSPELLINGS.put("tabble", "table");
        COMMON_MISSPELLINGS.put("datbase", "database");
        COMMON_MISSPELLINGS.put("qury", "query");
        COMMON_MISSPELLINGS.put("scehma", "schema");
        COMMON_MISSPELLINGS.put("colum", "column");

        // 初始化技术术语列表
        TECH_TERMS.add("SQL");
        TECH_TERMS.add("JOIN");
        TECH_TERMS.add("SELECT");
        TECH_TERMS.add("WHERE");
        TECH_TERMS.add("GROUP BY");
        TECH_TERMS.add("ORDER BY");
    }

    /**
     * 预处理文本
     *
     * @param text 原始文本
     * @return 预处理后的文本对象
     */
    @Override
    public PreprocessedText preprocess(String text) {
        // 使用setter方法替代构造函数
        if (text == null || text.trim().isEmpty()) {
            // 使用Builder模式而不是setter方法
            return PreprocessedText.builder()
                    .originalText("")
                    .normalizedText("")
                    .build();
        }

        try {
            // 1. 文本标准化
            String normalizedText = normalizeText(text);

            // 2. 语言检测
            String language = detectLanguage(text);

            // 3. 分词
            List<String> tokens = tokenize(normalizedText);

            // 4. 拼写检查
            List<CorrectionSuggestion> corrections = checkSpelling(normalizedText, tokens);

            // 5. 构建特征信息
            Map<String, TokenFeature> features = extractFeatures(tokens, language);

            // 创建并返回预处理结果（使用setter方法）
            return PreprocessedText.builder()
                    .originalText(text)
                    .normalizedText(normalizedText)
                    .tokens(tokens)
                    .language(language)
                    .tokenFeatures(features)
                    .corrections(corrections)
                    .build();

        } catch (Exception e) {
            log.warn("Error preprocessing text: " + e.getMessage(), e);
            // 发生异常时返回基本处理结果
            return PreprocessedText.builder()
                    .originalText(text)
                    .normalizedText(text.trim())
                    .build();
        }
    }

    /**
     * 文本标准化
     *
     * @param text 原始文本
     * @return 标准化后的文本
     */
    private String normalizeText(String text) {
        // 基本的文本标准化
        // 1. 去除多余空格
        // 2. 处理标点符号
        String normalizedText = text.trim().replaceAll("\\s+", " ");

        // 在实际实现中，这里应该使用TextNormalizer组件处理
        // 如果textNormalizer可用，使用textNormalizer组件
        // if (textNormalizer != null) {
        //     normalizedText = textNormalizer.normalize(text, NormalizerConfig.getNLQueryOptimized());
        // }

        return normalizedText;
    }

    /**
     * 检测文本语言
     *
     * @param text 输入文本
     * @return 检测到的语言代码
     */
    private String detectLanguage(String text) {
        // 简单的语言检测逻辑，检测是否包含中文字符
        if (text.matches(".*[\\u4e00-\\u9fa5]+.*")) {
            return "zh-CN";
        } else {
            return "en-US";
        }

        // 真实实现应该使用语言检测库，例如：
        // return languageDetector.detect(text);
    }

    /**
     * 文本分词
     *
     * @param text 标准化后的文本
     * @return 分词结果
     */
    private List<String> tokenize(String text) {
        // 简单的分词实现，按空格分割
        String[] words = text.split("\\s+");
        List<String> tokens = new ArrayList<>();
        for (String word : words) {
            if (!word.isEmpty()) {
                tokens.add(word);
            }
        }

        // 在实际实现中，这里应该使用Tokenizer组件进行分词
        // if (tokenizer != null) {
        //     return tokenizer.tokenize(text);
        // }

        return tokens;
    }

    /**
     * 拼写检查
     *
     * @param text   原始文本
     * @param tokens 分词结果
     * @return 拼写纠正建议列表
     */
    private List<CorrectionSuggestion> checkSpelling(String text, List<String> tokens) {
        List<CorrectionSuggestion> corrections = new ArrayList<>();

        // 简单的拼写检查实现，检查常见拼写错误
        for (String token : tokens) {
            String lowerToken = token.toLowerCase();
            if (COMMON_MISSPELLINGS.containsKey(lowerToken)) {
                String suggestion = COMMON_MISSPELLINGS.get(lowerToken);
                double confidence = 0.9;  // 置信度

                // 创建拼写纠正建议
                int startOffset = text.indexOf(token);
                CorrectionSuggestion correction;

                if (startOffset >= 0) {
                    // 使用Builder创建完整的拼写纠正建议
                    correction = CorrectionSuggestion.builder()
                            .originalText(token)
                            .suggestedText(suggestion)
                            .type(CorrectionType.SPELLING)
                            .level(CorrectionLevel.CRITICAL)
                            .reason("可能的拼写错误")
                            .confidence(confidence)
                            .startOffset(startOffset)
                            .endOffset(startOffset + token.length())
                            .build();
                } else {
                    // 如果找不到位置，使用工厂方法创建基本的拼写纠正建议
                    correction = CorrectionSuggestion.spelling(token, suggestion, confidence);
                }

                corrections.add(correction);
            }
        }

        // 在实际实现中，应该使用SpellChecker组件
        // return spellChecker.check(text, tokens);

        return corrections;
    }

    /**
     * 提取token特征
     *
     * @param tokens   分词结果
     * @param language 语言
     * @return token特征映射
     */
    private Map<String, TokenFeature> extractFeatures(List<String> tokens, String language) {
        Map<String, TokenFeature> features = new HashMap<>();

        // 为每个token创建基本特征
        for (String token : tokens) {
            // 创建词汇特征
            TokenFeature.LexicalFeature lexical = TokenFeature.LexicalFeature.builder()
                    .lemma(token.toLowerCase())  // 简化的词元化
                    .pos(guessPartOfSpeech(token))  // 简化的词性猜测
                    .stopWord(isStopWord(token))
                    .punctuation(isPunctuation(token))
                    .digit(isDigit(token))
                    .alphabetic(isAlphabetic(token))
                    .chinese(isChinese(token))
                    .build();

            // 创建token特征
            TokenFeature feature = TokenFeature.builder()
                    .lexical(lexical)
                    .build();

            features.put(token, feature);
        }

        return features;
    }

    /**
     * 简单的词性猜测
     *
     * @param token 单词
     * @return 猜测的词性
     */
    private String guessPartOfSpeech(String token) {
        // 这是一个非常简化的词性猜测，实际应使用POS Tagger
        if (token.endsWith("ing")) {
            return PosTag.TagType.VERB.name();  // 假设以ing结尾的是动词
        } else if (TECH_TERMS.contains(token.toUpperCase())) {
            return PosTag.TagType.NOUN.name();  // 假设技术术语是名词
        } else if (token.matches("\\d+")) {
            return PosTag.TagType.NUMERAL.name();  // 数字
        } else {
            return PosTag.TagType.OTHER.name();  // 未知
        }
    }

    /**
     * 检查是否为停用词
     */
    private boolean isStopWord(String token) {
        // 简化的停用词检查，实际应使用停用词表
        String lowerToken = token.toLowerCase();
        String[] commonStopWords = {"the", "a", "an", "and", "or", "but", "of", "with", "in", "on", "at", "to", "for"};
        for (String stopWord : commonStopWords) {
            if (stopWord.equals(lowerToken)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否为标点符号
     */
    private boolean isPunctuation(String token) {
        return token.matches("\\p{Punct}+");
    }

    /**
     * 检查是否为数字
     */
    private boolean isDigit(String token) {
        return token.matches("\\d+");
    }

    /**
     * 检查是否为字母
     */
    private boolean isAlphabetic(String token) {
        return token.matches("[a-zA-Z]+");
    }

    /**
     * 检查是否为中文
     */
    private boolean isChinese(String token) {
        return token.matches("[\\u4e00-\\u9fa5]+");
    }
}
