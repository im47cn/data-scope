package com.insightdata.nlquery.intent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 时间范围
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeRange {
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 时间单位
     */
    private TimeUnit timeUnit;
    
    /**
     * 时间值
     */
    private int timeValue;
    
    /**
     * 时间类型
     */
    private TimeType timeType;
    
    /**
     * 时间单位
     */
    public enum TimeUnit {
        /**
         * 天
         */
        DAY,
        
        /**
         * 周
         */
        WEEK,
        
        /**
         * 月
         */
        MONTH,
        
        /**
         * 季度
         */
        QUARTER,
        
        /**
         * 年
         */
        YEAR
    }
    
    /**
     * 时间类型
     */
    public enum TimeType {
        /**
         * 绝对时间范围
         */
        ABSOLUTE,
        
        /**
         * 相对时间范围
         */
        RELATIVE,
        
        /**
         * 未指定
         */
        UNSPECIFIED
    }
}
