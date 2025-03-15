package com.insightdata.nlquery.intent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 排序要求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SortRequirement {
    
    /**
     * 排序字段
     */
    private String field;
    
    /**
     * 排序方向
     */
    private SortDirection direction;
    
    /**
     * 排序优先级
     */
    private int priority;
    
    /**
     * 排序方向
     */
    public enum SortDirection {
        /**
         * 升序
         */
        ASC,
        
        /**
         * 降序
         */
        DESC
    }
}
