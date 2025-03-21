package com.insightdata.domain.nlquery.preprocess;

import com.insightdata.domain.nlquery.preprocess.normalizer.TextNormalizer;
import com.insightdata.domain.nlquery.preprocess.tokenizer.Tokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认文本预处理器实现
 */
@Component
public class DefaultTextPreprocessor implements TextPreprocessor {

    @Autowired
    private TextNormalizer textNormalizer;

    @Autowired
    private Tokenizer tokenizer;

    @Override
    public PreprocessedText preprocess(String text) {
        // 标准化文本
        String normalizedText = textNormalizer.normalize(text);

        // 分词
        List<String> tokens = tokenizer.tokenize(normalizedText);

        // 提取特征
        Map<String, TokenFeature> features = extractFeatures(tokens);

        // 纠正建议
        List<CorrectionSuggestion> corrections = generateCorrections(text);

        // 构建结果
        return PreprocessedText.builder()
                .originalText(text)
                .normalizedText(normalizedText)
                .tokens(tokens)
                .language(detectLanguage(text))
                .tokenFeatures(features)
                .corrections(corrections)
                .build();
    }

    private Map<String, TokenFeature> extractFeatures(List<String> tokens) {
        Map<String, TokenFeature> features = new HashMap<>();
        for (String token : tokens) {
            TokenFeature.LexicalFeature lexical = TokenFeature.LexicalFeature.builder()
                    .pos(getPOS(token))
                    .lemma(getLemma(token))
                    .isStopWord(isStopWord(token))
                    .length(token.length())
                    .containsDigit(token.matches(".*\\d+.*"))
                    .containsPunctuation(token.matches(".*\\p{Punct}+.*"))
                    .build();

            features.put(token, TokenFeature.builder()
                    .token(token)
                    .length(token.length())
                    .lexical(lexical)
                    .semantic(extractSemanticFeatures(token))
                    .build());
        }
        return features;
    }

    private List<CorrectionSuggestion> generateCorrections(String text) {
        List<CorrectionSuggestion> corrections = new ArrayList<>();
        // 示例: 检测并纠正常见错误
        if (text.contains("tabel")) {
            corrections.add(CorrectionSuggestion.builder()
                    .originalText("tabel")
                    .suggestedText("table")
                    .type(CorrectionSuggestion.CorrectionType.SPELLING)
                    .confidence(0.95)
                    .startOffset(text.indexOf("tabel"))
                    .endOffset(text.indexOf("tabel") + 5)
                    .build());
        }
        return corrections;
    }

    private String detectLanguage(String text) {
        // 简单的语言检测逻辑
        return text.matches(".*[\\u4e00-\\u9fa5]+.*") ? "zh-CN" : "en-US";
    }

    private String getPOS(String token) {
        // TODO: 实现词性标注
        return "UNKNOWN";
    }

    private String getLemma(String token) {
        // TODO: 实现词形还原
        return token.toLowerCase();
    }

    private boolean isStopWord(String token) {
        // TODO: 实现停用词检测
        return false;
    }

    private TokenFeature.SemanticFeature extractSemanticFeatures(String token) {
        // TODO: 实现语义特征提取
        return TokenFeature.SemanticFeature.builder()
                .wordVector("")
                .similarity(0.0)
                .category("UNKNOWN")
                .build();
    }
}
