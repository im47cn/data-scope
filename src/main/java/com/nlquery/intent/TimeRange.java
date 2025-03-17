package com.nlquery.intent;

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
     * 时间范围类型
     */
    public enum TimeRangeType {
        /**
         * 绝对时间范围
         */
        ABSOLUTE("绝对时间范围"),
        
        /**
         * 相对时间范围
         */
        RELATIVE("相对时间范围");
        
        private final String displayName;
        
        TimeRangeType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * 时间类型
     */
    public enum TimeType {
        /**
         * 绝对时间
         */
        ABSOLUTE("绝对时间"),
        
        /**
         * 相对时间
         */
        RELATIVE("相对时间"),
        
        /**
         * 时间点
         */
        POINT("时间点"),
        
        /**
         * 时间区间
         */
        INTERVAL("时间区间");
        
        private final String displayName;
        
        TimeType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * 时间单位
     */
    public enum TimeUnit {
        /**
         * 年
         */
        YEAR("年"),
        
        /**
         * 月
         */
        MONTH("月"),
        
        /**
         * 周
         */
        WEEK("周"),
        
        /**
         * 日
         */
        DAY("日"),
        
        /**
         * 时
         */
        HOUR("时"),
        
        /**
         * 分
         */
        MINUTE("分"),
        
        /**
         * 秒
         */
        SECOND("秒");
        
        private final String displayName;
        
        TimeUnit(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * 范围类型
     */
    private TimeRangeType rangeType;
    
    /**
     * 时间类型
     */
    private TimeType timeType;
    
    /**
     * 时间单位
     */
    private TimeUnit timeUnit;
    
    /**
     * 时间值
     */
    private int timeValue;
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 创建一个相对时间范围
     */
    public static TimeRange relative(int value, TimeUnit unit) {
        return TimeRange.builder()
                .rangeType(TimeRangeType.RELATIVE)
                .timeType(TimeType.RELATIVE)
                .timeUnit(unit)
                .timeValue(value)
                .build();
    }
    
    /**
     * 创建一个绝对时间范围
     */
    public static TimeRange absolute(LocalDateTime start, LocalDateTime end) {
        return TimeRange.builder()
                .rangeType(TimeRangeType.ABSOLUTE)
                .timeType(TimeType.INTERVAL)
                .startTime(start)
                .endTime(end)
                .build();
    }
    
    /**
     * 创建一个时间点
     */
    public static TimeRange point(LocalDateTime time) {
        return TimeRange.builder()
                .rangeType(TimeRangeType.ABSOLUTE)
                .timeType(TimeType.POINT)
                .startTime(time)
                .endTime(time)
                .build();
    }
    
    /**
     * 获取时间范围长度(秒)
     */
    public long getDurationSeconds() {
        if (startTime != null && endTime != null) {
            return java.time.Duration.between(startTime, endTime).getSeconds();
        }
        return -1;
    }
    
    /**
     * 是否是有效的时间范围
     */
    public boolean isValid() {
        if (rangeType == TimeRangeType.ABSOLUTE) {
            return startTime != null && endTime != null && !startTime.isAfter(endTime);
        } else {
            return timeUnit != null && timeValue > 0;
        }
    }
}
