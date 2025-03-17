package com.nlquery.preprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 词性标注类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PosTag {
    
    /**
     * 词语
     */
    private String word;
    
    /**
     * 词性
     */
    private String tag;
    
    /**
     * 开始位置
     */
    private Integer startPosition;
    
    /**
     * 结束位置
     */
    private Integer endPosition;
    
    /**
     * 置信度
     */
    private Double confidence;
    
    /**
     * 词性说明
     */
    private String description;
    
    /**
     * 是否是自定义词性
     */
    private Boolean isCustom;
    
    /**
     * 词性类型枚举
     */
    public enum TagType {
        /**
         * 名词
         */
        NOUN("n", "名词"),
        
        /**
         * 动词
         */
        VERB("v", "动词"),
        
        /**
         * 形容词
         */
        ADJECTIVE("adj", "形容词"),
        
        /**
         * 副词
         */
        ADVERB("adv", "副词"),
        
        /**
         * 数词
         */
        NUMERAL("num", "数词"),
        
        /**
         * 量词
         */
        MEASURE("m", "量词"),
        
        /**
         * 代词
         */
        PRONOUN("pron", "代词"),
        
        /**
         * 介词
         */
        PREPOSITION("prep", "介词"),
        
        /**
         * 连词
         */
        CONJUNCTION("conj", "连词"),
        
        /**
         * 助词
         */
        PARTICLE("part", "助词"),
        
        /**
         * 叹词
         */
        INTERJECTION("interj", "叹词"),
        
        /**
         * 标点符号
         */
        PUNCTUATION("punc", "标点符号"),
        
        /**
         * 其他
         */
        OTHER("other", "其他");
        
        /**
         * 标签代码
         */
        private final String code;
        
        /**
         * 标签名称
         */
        private final String name;
        
        TagType(String code, String name) {
            this.code = code;
            this.name = name;
        }
        
        public String getCode() {
            return code;
        }
        
        public String getName() {
            return name;
        }
        
        /**
         * 根据代码获取标签类型
         */
        public static TagType fromCode(String code) {
            for (TagType type : values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
            return OTHER;
        }
    }
}
