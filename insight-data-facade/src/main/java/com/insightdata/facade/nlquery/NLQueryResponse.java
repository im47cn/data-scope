package com.insightdata.facade.nlquery;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自然语言查询响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NLQueryResponse {
    
    /**
     * 查询ID
     */
    private String queryId;
    
    /**
     * 原始自然语言查询
     */
    private String originalQuery;
    
    /**
     * 生成的SQL语句
     */
    private String generatedSql;
    
    /**
     * 查询结果数据
     */
    private List<Map<String, Object>> data;
    
    /**
     * 结果列元数据
     */
    private List<ColumnMetadata> columns;
    
    /**
     * 执行时间（毫秒）
     */
    private Long executionTime;
    
    /**
     * 总行数
     */
    private Long totalRows;
    
    /**
     * 返回的行数
     */
    private Integer returnedRows;
    
    /**
     * 结果是否被截断
     */
    private Boolean truncated;
    
    /**
     * 查询状态（成功/失败）
     */
    private String status;
    
    /**
     * 错误信息（如果失败）
     */
    private String errorMessage;
    
    /**
     * 转换置信度（0-1）
     */
    private Double confidenceScore;
    
    /**
     * SQL解释（如果请求）
     */
    private String sqlExplanation;
    
    /**
     * 查询计划（如果请求）
     */
    private String queryPlan;
    
    /**
     * 上下文ID
     */
    private String contextId;
    
    /**
     * 时间戳
     */
    private LocalDateTime timestamp;
    
    /**
     * 列元数据
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ColumnMetadata {
        
        /**
         * 列名
         */
        private String name;
        
        /**
         * 显示名称
         */
        private String displayName;
        
        /**
         * 数据类型
         */
        private String dataType;
        
        /**
         * 是否为主键
         */
        private Boolean isPrimaryKey;
        
        /**
         * 表名
         */
        private String tableName;
        
        /**
         * 模式名
         */
        private String schemaName;
        
        /**
         * 是否可为空
         */
        private Boolean nullable;
    }
}