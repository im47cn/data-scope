package com.domain.model.query;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryHistory {
    
    /**
     * 查询历史ID
     */
    private String id;
    
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 查询语句
     */
    private String query;
    
    /**
     * SQL语句
     */
    private String sql;
    
    /**
     * 执行时间
     */
    private LocalDateTime executedAt;
    
    /**
     * 执行时长(毫秒)
     */
    private Long duration;
    
    /**
     * 结果行数
     */
    private Long resultCount;
    
    /**
     * 是否成功
     */
    private Boolean success;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 创建人
     */
    private String createdBy;
    
    /**
     * 更新人
     */
    private String updatedBy;
}
