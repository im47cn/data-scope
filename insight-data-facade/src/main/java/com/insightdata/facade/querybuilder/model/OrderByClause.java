package com.insightdata.facade.querybuilder.model;

import lombok.Data;

@Data
public class OrderByClause {
    private String field;
    private String expression;
    private Direction direction;
    private NullOrder nullOrder;

    public enum Direction {
        ASC,
        DESC
    }

    public enum NullOrder {
        NULLS_FIRST,
        NULLS_LAST
    }
}