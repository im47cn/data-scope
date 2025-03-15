package com.insightdata.nlquery.preprocess;

/**
 * 词性标注
 * 表示单词的词性信息
 */
public class PosTag {
    
    // 单词
    private String word;
    
    // 词性
    private String tag;
    
    /**
     * 构造函数
     *
     * @param word 单词
     * @param tag 词性
     */
    public PosTag(String word, String tag) {
        this.word = word;
        this.tag = tag;
    }
    
    /**
     * 获取单词
     *
     * @return 单词
     */
    public String getWord() {
        return word;
    }
    
    /**
     * 设置单词
     *
     * @param word 单词
     */
    public void setWord(String word) {
        this.word = word;
    }
    
    /**
     * 获取词性
     *
     * @return 词性
     */
    public String getTag() {
        return tag;
    }
    
    /**
     * 设置词性
     *
     * @param tag 词性
     */
    public void setTag(String tag) {
        this.tag = tag;
    }
    
    @Override
    public String toString() {
        return word + "/" + tag;
    }
}
