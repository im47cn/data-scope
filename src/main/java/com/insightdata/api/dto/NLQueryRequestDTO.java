package com.insightdata.api.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自然语言查询请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NLQueryRequestDTO {
    
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 自然语言查询
     */
    private String query;
    
    /**
     * 查询参数
     */
    private Map<String, Object> parameters;
}