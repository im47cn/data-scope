package com.insightdata.nlquery.intent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 限制条件
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LimitRequirement {
    
    /**
     * 限制类型
     */
    private LimitType limitType;
    
    /**
     * 限制值
     */
    private int limitValue;
    
    /**
     * 偏移量
     */
    private int offset;
    
    /**
     * 限制类型
     */
    public enum LimitType {
        /**
         * 前N条
         */
        TOP_N,
        
        /**
         * 分页
         */
        PAGINATION,
        
        /**
         * 无限制
         */
        NONE
    }
}
