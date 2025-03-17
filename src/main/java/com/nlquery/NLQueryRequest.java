package com.nlquery;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NLQueryRequest {
    
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 查询语句
     */
    private String query;
    
    /**
     * 查询参数
     */
    private Map<String, Object> parameters;
    
    /**
     * 查询标签
     */
    private List<String> tags;
}