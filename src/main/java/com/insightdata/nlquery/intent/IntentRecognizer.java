package com.insightdata.nlquery.intent;

import com.insightdata.nlquery.preprocess.PreprocessedText;

/**
 * 意图识别器接口
 */
public interface IntentRecognizer {
    
    /**
     * 识别查询意图
     *
     * @param preprocessedText 预处理后的文本
     * @return 查询意图
     */
    QueryIntent recognizeIntent(PreprocessedText preprocessedText);
    
    /**
     * 识别查询意图
     *
     * @param preprocessedText 预处理后的文本
     * @param context 意图识别上下文
     * @return 查询意图
     */
    QueryIntent recognizeIntent(PreprocessedText preprocessedText, IntentRecognitionContext context);
}
