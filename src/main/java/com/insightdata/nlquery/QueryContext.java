package com.insightdata.nlquery;

import com.insightdata.domain.model.DataSource;
import com.insightdata.domain.model.metadata.SchemaInfo;
import com.insightdata.nlquery.entity.EntityTag;
import com.insightdata.nlquery.intent.QueryIntent;
import com.insightdata.nlquery.preprocess.PreprocessedText;
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
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryContext {
    
    /**
     * 数据源
     */
    private DataSource dataSource;
    
    /**
     * 元数据信息
     */
    private SchemaInfo metadata;
    
    /**
     * 原始查询
     */
    private NLQueryRequest request;
    
    /**
     * 预处理结果
     */
    private PreprocessedText preprocessedText;
    
    /**
     * 查询意图
     */
    private QueryIntent queryIntent;
    
    /**
     * 提取的实体
     */
    @Builder.Default
    private List<EntityTag> entities = new ArrayList<>();
    
    /**
     * 上下文参数
     */
    @Builder.Default
    private Map<String, Object> parameters = new HashMap<>();
    
    /**
     * 是否使用缓存
     */
    @Builder.Default
    private boolean useCache = true;
    
    /**
     * 缓存过期时间(秒)
     */
    @Builder.Default
    private int cacheExpireSeconds = 300;
    
    /**
     * 是否返回总行数
     */
    @Builder.Default
    private boolean fetchTotalRows = false;
    
    /**
     * 最大返回行数
     */
    private Integer maxRows;
    
    /**
     * 查询超时时间(秒)
     */
    private Integer queryTimeout;
    
    /**
     * 创建一个简单的查询上下文
     */
    public static QueryContext simple(DataSource dataSource, NLQueryRequest request) {
        return QueryContext.builder()
                .dataSource(dataSource)
                .request(request)
                .build();
    }
    
    /**
     * 创建一个带元数据的查询上下文
     */
    public static QueryContext withMetadata(DataSource dataSource, SchemaInfo metadata, NLQueryRequest request) {
        return QueryContext.builder()
                .dataSource(dataSource)
                .metadata(metadata)
                .request(request)
                .build();
    }
    
    /**
     * 添加参数
     */
    public QueryContext addParameter(String key, Object value) {
        parameters.put(key, value);
        return this;
    }
    
    /**
     * 添加实体
     */
    public QueryContext addEntity(EntityTag entity) {
        entities.add(entity);
        return this;
    }
    
    /**
     * 获取数据源ID
     */
    public Long getDataSourceId() {
        return dataSource != null ? dataSource.getId() : null;
    }
    
    /**
     * 获取数据源名称
     */
    public String getDataSourceName() {
        return dataSource != null ? dataSource.getName() : null;
    }
    
    /**
     * 获取数据源类型
     */
    public String getDataSourceType() {
        return dataSource != null ? dataSource.getType() : null;
    }
    
    /**
     * 获取原始查询文本
     */
    public String getOriginalQuery() {
        return request != null ? request.getQuery() : null;
    }
    
    /**
     * 获取标准化查询文本
     */
    public String getNormalizedQuery() {
        return preprocessedText != null ? preprocessedText.getNormalizedText() : null;
    }
}