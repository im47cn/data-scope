package com.insightdata.nlquery.preprocess;

/**
 * 实体标注
 * 表示文本中识别出的实体信息
 */
public class EntityTag {
    
    // 实体文本
    private String text;
    
    // 实体类型
    private EntityType type;
    
    // 开始位置
    private int startPosition;
    
    // 结束位置
    private int endPosition;
    
    // 置信度
    private double confidence;
    
    /**
     * 构造函数
     *
     * @param text 实体文本
     * @param type 实体类型
     * @param startPosition 开始位置
     * @param endPosition 结束位置
     */
    public EntityTag(String text, EntityType type, int startPosition, int endPosition) {
        this.text = text;
        this.type = type;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.confidence = 1.0;
    }
    
    /**
     * 构造函数
     *
     * @param text 实体文本
     * @param type 实体类型
     * @param startPosition 开始位置
     * @param endPosition 结束位置
     * @param confidence 置信度
     */
    public EntityTag(String text, EntityType type, int startPosition, int endPosition, double confidence) {
        this.text = text;
        this.type = type;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.confidence = confidence;
    }
    
    /**
     * 获取实体文本
     *
     * @return 实体文本
     */
    public String getText() {
        return text;
    }
    
    /**
     * 设置实体文本
     *
     * @param text 实体文本
     */
    public void setText(String text) {
        this.text = text;
    }
    
    /**
     * 获取实体类型
     *
     * @return 实体类型
     */
    public EntityType getType() {
        return type;
    }
    
    /**
     * 设置实体类型
     *
     * @param type 实体类型
     */
    public void setType(EntityType type) {
        this.type = type;
    }
    
    /**
     * 获取开始位置
     *
     * @return 开始位置
     */
    public int getStartPosition() {
        return startPosition;
    }
    
    /**
     * 设置开始位置
     *
     * @param startPosition 开始位置
     */
    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }
    
    /**
     * 获取结束位置
     *
     * @return 结束位置
     */
    public int getEndPosition() {
        return endPosition;
    }
    
    /**
     * 设置结束位置
     *
     * @param endPosition 结束位置
     */
    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }
    
    /**
     * 获取置信度
     *
     * @return 置信度
     */
    public double getConfidence() {
        return confidence;
    }
    
    /**
     * 设置置信度
     *
     * @param confidence 置信度
     */
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
    
    @Override
    public String toString() {
        return text + "[" + type + "]";
    }
}
