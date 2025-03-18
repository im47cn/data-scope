package com.insightdata.facade.metadata.enums;

/**
 * 同步状态枚举
 */
public enum SyncStatus {
    /**
     * 等待中
     */
    PENDING("等待中"),
    
    /**
     * 运行中
     */
    RUNNING("运行中"),
    
    /**
     * 已完成
     */
    COMPLETED("已完成"),
    
    /**
     * 失败
     */
    FAILED("失败"),
    
    /**
     * 已取消
     */
    CANCELLED("已取消");
    
    private final String displayName;
    
    SyncStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}