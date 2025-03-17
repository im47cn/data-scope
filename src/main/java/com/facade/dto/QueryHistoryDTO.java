package com.facade.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询历史DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryHistoryDTO {
    
    /**
     * 查询历史ID
     */
    private String id;
    
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 数据源名称
     */
    private String dataSourceName;
    
    /**
     * 原始查询（自然语言或SQL）
     */
    private String originalQuery;
    
    /**
     * 执行的SQL
     */
    private String executedSql;
    
    /**
     * 查询参数
     */
    private Map<String, Object> parameters;
    
    /**
     * 执行时间（毫秒）
     */
    private Long executionTime;
    
    /**
     * 返回的行数
     */
    private Long rowCount;
    
    /**
     * 查询状态（成功/失败）
     */
    private String status;
    
    /**
     * 错误信息（如果失败）
     */
    private String errorMessage;
    
    /**
     * 查询类型（NL/SQL）
     */
    private String queryType;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 执行时间
     */
    private LocalDateTime executedAt;
    
    /**
     * 是否已保存为常用查询
     */
    private Boolean isSaved;
    
    /**
     * 标签
     */
    private String[] tags;
}