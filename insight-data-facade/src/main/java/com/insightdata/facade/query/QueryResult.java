package com.insightdata.facade.query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询结果DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryResult {
    
    /**
     * 是否查询成功
     */
    private boolean success;
    
    /**
     * 查询执行状态消息
     */
    private String message;
    
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 元数据列信息
     */
    private List<ColumnMetadata> columns;
    
    /**
     * 查询结果数据（行列形式）
     */
    private List<List<Object>> rows;
    
    /**
     * 查询结果数据（记录对象形式）
     */
    private List<Map<String, Object>> records;
    
    /**
     * 查询执行时间（毫秒）
     */
    private Long executionTime;
    
    /**
     * 总行数（可能超过返回的行数）
     */
    private Long totalRows;
    
    /**
     * 当前页码
     */
    private Integer page;
    
    /**
     * 每页行数
     */
    private Integer pageSize;
    
    /**
     * 总页数
     */
    private Integer totalPages;
    
    /**
     * 查询执行时间
     */
    private LocalDateTime executedAt;
    
    /**
     * 查询计划信息
     */
    private String queryPlan;
    
    /**
     * 错误信息（如果查询失败）
     */
    private String errorMessage;
    
    /**
     * 原始SQL
     */
    private String sql;
    
    /**
     * 列元数据信息类
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ColumnMetadata {
        
        /**
         * 列名
         */
        private String name;
        
        /**
         * 列标签
         */
        private String label;
        
        /**
         * 数据类型
         */
        private String dataType;
        
        /**
         * 数据类型名称
         */
        private String typeName;
        
        /**
         * 精度
         */
        private Integer precision;
        
        /**
         * 小数位数
         */
        private Integer scale;
        
        /**
         * 是否允许为空
         */
        private Boolean nullable;
        
        /**
         * 是否为自增列
         */
        private Boolean autoIncrement;
        
        /**
         * 表名
         */
        private String tableName;
        
        /**
         * 模式名
         */
        private String schemaName;
    }
}