package com.insightdata.domain.nlquery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询上下文
 * 包含查询过程中的所有信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryContext {
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 自然语言查询（预处理后）
     */
    private String nlQuery;
    
    /**
     * 上下文ID
     */
    private String contextId;
    
    /**
     * 检测到的意图
     */
    private String intent;
    
    /**
     * 意图置信度
     */
    private Double confidence;
    
    /**
     * 实体列表
     */
    @Builder.Default
    private List<Map<String, Object>> entities = new ArrayList<>();
    
    /**
     * 生成的SQL
     */
    private String sql;
    
    /**
     * 查询参数
     */
    @Builder.Default
    private Map<String, Object> parameters = new HashMap<>();
    
    /**
     * 查询选项
     */
    @Builder.Default
    private Map<String, Object> options = new HashMap<>();
    
    /**
     * 查询结果数据
     */
    @Builder.Default
    private List<Map<String, Object>> data = new ArrayList<>();
    
    /**
     * 查询结果元数据
     */
    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();
    
    /**
     * 建议列表
     */
    @Builder.Default
    private List<String> suggestions = new ArrayList<>();
}