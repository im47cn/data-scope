package com.insightdata.domain.nlquery.intent;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimeRange {
    public enum TimeType {
        ABSOLUTE,  // 绝对时间范围
        RELATIVE   // 相对时间范围
    }

    public enum TimeUnit {
        MINUTE,
        HOUR,
        DAY,
        WEEK,
        MONTH,
        QUARTER,
        YEAR
    }

    private TimeType timeType;
    private TimeUnit timeUnit;
    private int timeValue;
    private String startTime;  // 用于绝对时间范围
    private String endTime;    // 用于绝对时间范围

    public static TimeRange createRelativeTimeRange(TimeUnit unit, int value) {
        return TimeRange.builder()
                .timeType(TimeType.RELATIVE)
                .timeUnit(unit)
                .timeValue(value)
                .build();
    }

    public static TimeRange createAbsoluteTimeRange(String start, String end) {
        return TimeRange.builder()
                .timeType(TimeType.ABSOLUTE)
                .startTime(start)
                .endTime(end)
                .build();
    }

    public boolean isRelative() {
        return timeType == TimeType.RELATIVE;
    }

    public boolean isAbsolute() {
        return timeType == TimeType.ABSOLUTE;
    }
}
