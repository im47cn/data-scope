package com.insightdata.domain.metadata.enums;

/**
 * 同步类型枚举
 */
public enum SyncType {
    /**
     * 全量同步
     */
    FULL("全量同步"),
    
    /**
     * 增量同步
     */
    INCREMENTAL("增量同步"),
    
    /**
     * 元数据同步
     */
    METADATA_ONLY("仅元数据同步");
    
    private final String displayName;
    
    SyncType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}