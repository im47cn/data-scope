package com.insightdata.facade.querybuilder.model;

import java.util.List;
import lombok.Data;

@Data
public class FilterGroup {
    public enum LogicalOperator {
        AND,
        OR
    }

    public static class FilterCondition {
        private String field;
        private String operator;
        private Object value;
        private Object secondValue; // For BETWEEN operator
        private boolean caseSensitive;
        private boolean negate;
        private String expression;
    }

    private LogicalOperator operator;
    private List<FilterCondition> conditions;
    private List<FilterGroup> groups;
    private boolean negate;
    private String customExpression;
}