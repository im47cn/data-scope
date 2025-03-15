package com.insightdata.domain.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 自然语言查询请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NLQueryRequest {
    
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
