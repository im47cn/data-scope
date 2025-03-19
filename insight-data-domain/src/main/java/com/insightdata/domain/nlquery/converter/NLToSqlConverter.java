package com.insightdata.domain.nlquery.converter;

import com.insightdata.domain.nlquery.NLQueryRequest;
import com.insightdata.domain.nlquery.QueryContext;

public interface NLToSqlConverter {
    // 保留当前接口用于兼容性
    SqlConversionResult convert(NLQueryRequest request);
    
    // 添加文档中定义的方法，但包装为使用现有实现
    default String convert(String naturalLanguageQuery, Long dataSourceId) {
        NLQueryRequest request = NLQueryRequest.builder()
            .query(naturalLanguageQuery)
            .dataSourceId(dataSourceId.toString())
            .build();
        return convert(request).getSql();
    }
    
    default String convert(String naturalLanguageQuery, Long dataSourceId, QueryContext context) {
        NLQueryRequest request = NLQueryRequest.builder()
            .query(naturalLanguageQuery)
            .dataSourceId(dataSourceId.toString())
            .contextId(context.getContextId())
            .build();
        return convert(request).getSql();
    }
}