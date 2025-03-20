package com.insightdata.domain.nlquery.intent;

/**
 * 查询类型枚举
 */
public enum QueryType {
    SELECT(false, false, false),
    COUNT(false, false, true),
    SUM(true, false, true),
    AVG(true, false, true),
    MAX(false, true, true),
    MIN(false, true, true),
    GROUP(true, false, false),
    UNKNOWN(false, false, false);

    private final boolean needsGrouping;
    private final boolean needsSorting;
    private final boolean needsLimit;

    QueryType(boolean needsGrouping, boolean needsSorting, boolean needsLimit) {
        this.needsGrouping = needsGrouping;
        this.needsSorting = needsSorting;
        this.needsLimit = needsLimit;
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
