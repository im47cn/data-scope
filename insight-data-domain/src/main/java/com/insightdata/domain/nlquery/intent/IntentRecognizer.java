package com.insightdata.domain.nlquery.intent;

import com.insightdata.domain.nlquery.preprocess.PreprocessedText;

/**
 * 意图识别器接口
 */
public interface IntentRecognizer {
    
    /**
     * 识别意图
     *
     * @param preprocessedText 预处理后的文本
     * @param context 意图识别上下文
     * @return 查询意图
     */
    QueryIntent recognizeIntent(PreprocessedText preprocessedText, IntentRecognitionContext context);
    
    /**
     * 识别意图(使用默认上下文)
     *
     * @param preprocessedText 预处理后的文本
     * @return 查询意图
     */
    default QueryIntent recognizeIntent(PreprocessedText preprocessedText) {
        return recognizeIntent(preprocessedText, new IntentRecognitionContext());
    }
}
