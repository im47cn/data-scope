package com.nlquery;

import com.domain.model.DataSource;
import com.nlquery.entity.EntityExtractionContext;
import com.nlquery.intent.IntentRecognitionContext;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
     * 查询请求
     */
    private NLQueryRequest request;
    
    /**
     * 实体提取上下文
     */
    private EntityExtractionContext entityContext;
    
    /**
     * 意图识别上下文
     */
    private IntentRecognitionContext intentContext;
    
    /**
     * 是否需要缓存结果
     */
    private boolean cacheResult;
    
    /**
     * 超时时间(毫秒)
     */
    private Long timeout;
    
    /**
     * 最大返回行数
     */
    private Integer maxRows;
    
    /**
     * 是否返回元数据
     */
    private boolean includeMetadata;
}