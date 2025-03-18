package com.insightdata.domain.nlquery.preprocess;

/**
 * 文本纠正级别枚举
 * 用于标识纠正建议的严重程度
 */
public enum CorrectionLevel {

    /**
     * 严重
     * 必须修正的错误,会影响理解或执行
     */
    CRITICAL(3),

    /**
     * 警告
     * 建议修正的问题,可能影响准确性
     */
    WARNING(2),

    /**
     * 建议
     * 可以改进的地方,但不影响基本功能
     */
    SUGGESTION(1);

    /**
     * 级别权重
     */
    private final int weight;

    /**
     * 构造函数
     * @param weight 级别权重
     */
    CorrectionLevel(int weight) {
        this.weight = weight;
    }

    /**
     * 获取级别权重
     * @return 权重值
     */
    public int getWeight() {
        return weight;
    }

    /**
     * 是否为严重级别
     * @return 如果是CRITICAL返回true
     */
    public boolean isCritical() {
        return this == CRITICAL;
    }

    /**
     * 是否为警告级别
     * @return 如果是WARNING返回true
     */
    public boolean isWarning() {
        return this == WARNING;
    }

    /**
     * 是否为建议级别
     * @return 如果是SUGGESTION返回true
     */
    public boolean isSuggestion() {
        return this == SUGGESTION;
    }

    /**
     * 比较级别
     * @param other 待比较的级别
     * @return 如果当前级别更严重返回true
     */
    public boolean isMoreSevereThan(CorrectionLevel other) {
        return this.weight > other.weight;
    }

    /**
     * 根据级别获取默认描述
     * @return 对应级别的描述文本
     */
    public String getDescription() {
        switch (this) {
            case CRITICAL:
                return "严重错误,必须修正";
            case WARNING:
                return "警告,建议修正";
            case SUGGESTION:
                return "建议,可以改进";
            default:
                return "未知级别";
        }
    }
}