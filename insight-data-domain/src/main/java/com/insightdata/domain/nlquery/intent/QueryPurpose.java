package com.insightdata.domain.nlquery.intent;

/**
 * 查询目的枚举
 */
public enum QueryPurpose {
    DATA_RETRIEVAL(false, false, false, false),
    STATISTICAL_ANALYSIS(true, false, true, true),
    TREND_ANALYSIS(true, true, true, true),
    COMPARISON_ANALYSIS(true, true, true, true),
    ANOMALY_DETECTION(true, true, true, true);

    private final boolean needsTimeRange;
    private final boolean needsGrouping;
    private final boolean needsSorting;
    private final boolean needsLimit;

    QueryPurpose(boolean needsTimeRange, boolean needsGrouping, boolean needsSorting, boolean needsLimit) {
        this.needsTimeRange = needsTimeRange;
        this.needsGrouping = needsGrouping;
        this.needsSorting = needsSorting;
        this.needsLimit = needsLimit;
    }

    public boolean needsTimeRange() {
        return needsTimeRange;
    }

    public boolean needsGrouping() {
        return needsGrouping;
    }

    public boolean needsSorting() {
        return needsSorting;
    }

    public boolean needsLimit() {
        return needsLimit;
    }
}
