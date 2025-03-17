package com.facade.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 表关系数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class TableRelationshipDTO {
    
    /**
     * 关系ID
     */
    private Long id;
    
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 源表
     */
    private String sourceTable;
    
    /**
     * 源列
     */
    private String sourceColumn;
    
    /**
     * 目标表
     */
    private String targetTable;
    
    /**
     * 目标列
     */
    private String targetColumn;
    
    /**
     * 关系类型（ONE_TO_ONE、ONE_TO_MANY、MANY_TO_ONE、MANY_TO_MANY）
     */
    private String type;
    
    /**
     * 关系来源（METADATA、QUERY_HISTORY、USER_FEEDBACK）
     */
    private String source;
    
    /**
     * 关系权重（0-1）
     */
    private Double weight;
    
    /**
     * 使用频率
     */
    private Integer frequency;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}