package com.insightdata.domain.nlquery.preprocess;

/**
 * 文本纠正类型枚举
 * 用于标识不同类型的文本纠正
 */
public enum CorrectionType {

    /**
     * 拼写纠正
     * 修正拼写错误、标点符号等问题
     */
    SPELLING("拼写"),

    /**
     * 语法纠正
     * 修正语法错误、句子结构等问题
     */
    GRAMMAR("语法"),

    /**
     * 格式纠正
     * 修正文本格式、排版等问题
     */
    FORMAT("格式"),

    /**
     * 术语纠正
     * 修正专业术语、命名等问题
     */
    TERMINOLOGY("术语"),

    /**
     * 语义纠正
     * 修正语义不清、歧义等问题
     */
    SEMANTIC("语义"),

    /**
     * 完整性纠正
     * 修正信息缺失、不完整等问题
     */
    COMPLETENESS("完整性");

    /**
     * 纠正类型的描述
     */
    private final String description;

    /**
     * 构造函数
     * @param description 类型描述
     */
    CorrectionType(String description) {
        this.description = description;
    }

    /**
     * 获取类型描述
     * @return 类型描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 是否为拼写纠正
     * @return 如果是SPELLING返回true
     */
    public boolean isSpelling() {
        return this == SPELLING;
    }

    /**
     * 是否为语法纠正
     * @return 如果是GRAMMAR返回true
     */
    public boolean isGrammar() {
        return this == GRAMMAR;
    }

    /**
     * 是否为格式纠正
     * @return 如果是FORMAT返回true
     */
    public boolean isFormat() {
        return this == FORMAT;
    }

    /**
     * 是否为术语纠正
     * @return 如果是TERMINOLOGY返回true
     */
    public boolean isTerminology() {
        return this == TERMINOLOGY;
    }

    /**
     * 是否为语义纠正
     * @return 如果是SEMANTIC返回true
     */
    public boolean isSemantic() {
        return this == SEMANTIC;
    }

    /**
     * 是否为完整性纠正
     * @return 如果是COMPLETENESS返回true
     */
    public boolean isCompleteness() {
        return this == COMPLETENESS;
    }

    /**
     * 根据描述获取纠正类型
     *
     * @param description 类型描述
     * @return 对应的纠正类型,如果没找到返回null
     */
    public static CorrectionType fromDescription(String description) {
        if (description == null) {
            return null;
        }
        for (CorrectionType type : values()) {
            if (type.getDescription().equals(description)) {
                return type;
            }
        }
        return null;
    }
}
