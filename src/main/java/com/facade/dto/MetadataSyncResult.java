package com.facade.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 元数据同步结果DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetadataSyncResult {
    
    /**
     * 是否同步成功
     */
    private boolean success;
    
    /**
     * 同步作业ID
     */
    private String jobId;
    
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 同步状态信息
     */
    private String message;
    
    /**
     * 同步的模式数量
     */
    private Integer schemaCount;
    
    /**
     * 同步的表数量
     */
    private Integer tableCount;
    
    /**
     * 同步的列数量
     */
    private Integer columnCount;
    
    /**
     * 同步开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 同步结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 进度（百分比）
     */
    private Integer progress;
    
    /**
     * 下一步操作提示
     */
    private String nextAction;
}