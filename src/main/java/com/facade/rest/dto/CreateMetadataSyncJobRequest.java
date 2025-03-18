package com.facade.rest.dto;

import com.common.enums.SyncType;
import javax.validation.constraints.NotNull;

/**
 * 创建元数据同步作业请求DTO
 */
public class CreateMetadataSyncJobRequest {
    
    /**
     * 数据源ID
     */
    @NotNull(message = "数据源ID不能为空")
    private String dataSourceId;
    
    /**
     * 同步类型
     */
    @NotNull(message = "同步类型不能为空")
    private SyncType type;
    
    public CreateMetadataSyncJobRequest() {
    }
    
    public CreateMetadataSyncJobRequest(String dataSourceId, SyncType type) {
        this.dataSourceId = dataSourceId;
        this.type = type;
    }
    
    public String getDataSourceId() {
        return dataSourceId;
    }
    
    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }
    
    public SyncType getType() {
        return type;
    }
    
    public void setType(SyncType type) {
        this.type = type;
    }
}