package com.insightdata.facade.rest.dto;

import com.insightdata.common.enums.SyncType;
import jakarta.validation.constraints.NotNull;

/**
 * 创建元数据同步作业请求DTO
 */
public class CreateMetadataSyncJobRequest {
    
    /**
     * 数据源ID
     */
    @NotNull(message = "数据源ID不能为空")
    private Long dataSourceId;
    
    /**
     * 同步类型
     */
    @NotNull(message = "同步类型不能为空")
    private SyncType type;
    
    public CreateMetadataSyncJobRequest() {
    }
    
    public CreateMetadataSyncJobRequest(Long dataSourceId, SyncType type) {
        this.dataSourceId = dataSourceId;
        this.type = type;
    }
    
    public Long getDataSourceId() {
        return dataSourceId;
    }
    
    public void setDataSourceId(Long dataSourceId) {
        this.dataSourceId = dataSourceId;
    }
    
    public SyncType getType() {
        return type;
    }
    
    public void setType(SyncType type) {
        this.type = type;
    }
}