package com.insightdata.domain.nlquery.preprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文本纠正建议
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorrectionSuggestion {

    /**
     * 原始文本
     */
    private String originalText;

    /**
     * 建议的纠正文本
     */
    private String suggestedText;

    /**
     * 纠正类型
     */
    private CorrectionType type;

    /**
     * 置信度
     */
    private double confidence;

    /**
     * 开始位置
     */
    private int startOffset;

    /**
     * 结束位置
     */
    private int endOffset;

    /**
     * 纠正类型枚举
     */
    public enum CorrectionType {
        SPELLING,       // 拼写错误
        GRAMMAR,        // 语法错误
        PUNCTUATION,   // 标点符号
        WORD_CHOICE,   // 词语选择
        FORMAT         // 格式问题
    }
}
