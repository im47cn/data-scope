package com.insightdata.nlquery.preprocess;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * 词性标注
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PosTag {
    
    /**
     * 词语
     */
    String word;
    
    /**
     * 词性
     */
    String tag;
    
    /**
     * 开始位置
     */
    int startIndex;
    
    /**
     * 结束位置
     */
    int endIndex;
    
    /**
     * 置信度分数(0-1)
     */
    double confidence;
    
    /**
     * 词性说明
     */
    String description;
    
    /**
     * 创建一个简单的词性标注
     */
    public PosTag(String word, String tag) {
        this.word = word;
        this.tag = tag;
        this.confidence = 1.0;
    }
}
