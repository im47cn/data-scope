package com.insightdata.domain.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 查询历史
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryHistory {
    
    /**
     * 查询历史ID
     */
    private Long id;
    
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 自然语言查询
     */
    private String naturalLanguageQuery;
    
    /**
     * 生成的SQL
     */
    private String generatedSql;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 状态（成功/失败）
     */
    private String status;
    
    /**
     * 执行时间（毫秒）
     */
    private Long executionTime;
    
    /**
     * 错误消息
     */
    private String errorMessage;
}
