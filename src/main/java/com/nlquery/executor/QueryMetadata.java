package com.nlquery.executor;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询元数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QueryMetadata {
    
    /**
     * 查询ID
     */
    String queryId;
    
    /**
     * 查询类型
     */
    QueryType queryType;
    
    /**
     * 查询参数
     */
    @Builder.Default
    Map<String, Object> parameters = new HashMap<>();
    
    /**
     * 是否缓存结果
     */
    @Builder.Default
    boolean cacheResult = false;
    
    /**
     * 缓存过期时间(秒)
     */
    @Builder.Default
    int cacheExpireSeconds = 300;
    
    /**
     * 是否异步执行
     */
    @Builder.Default
    boolean async = false;
    
    /**
     * 超时时间(毫秒)
     */
    @Builder.Default
    int timeout = 30000;
    
    /**
     * 最大返回行数
     */
    @Builder.Default
    int maxRows = 1000;
    
    /**
     * 是否返回总行数
     */
    @Builder.Default
    boolean returnTotalRows = false;
    
    /**
     * 是否返回元数据
     */
    @Builder.Default
    boolean returnMetadata = false;
    
    /**
     * 查询标签
     */
    @Builder.Default
    List<String> tags = new ArrayList<>();
    
    /**
     * 查询类型枚举
     */
    public enum QueryType {
        /**
         * 查询
         */
        QUERY,
        
        /**
         * 更新
         */
        UPDATE,
        
        /**
         * 批量更新
         */
        BATCH_UPDATE,
        
        /**
         * 存储过程
         */
        PROCEDURE,
        
        /**
         * 函数
         */
        FUNCTION
    }
}