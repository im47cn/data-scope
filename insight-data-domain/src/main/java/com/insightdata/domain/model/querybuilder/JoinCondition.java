package com.insightdata.domain.model.querybuilder;

public class JoinCondition {
    private String fromTable;
    private String fromColumn;
    private String toTable;
    private String toColumn;
    private String joinType; // e.g., INNER, LEFT, RIGHT

    public String getFromTable() {
        return fromTable;
    }

    public void setFromTable(String fromTable) {
        this.fromTable = fromTable;
    }

    public String getFromColumn() {
        return fromColumn;
    }

    public void setFromColumn(String fromColumn) {
        this.fromColumn = fromColumn;
    }

    public String getToTable() {
        return toTable;
    }

    public void setToTable(String toTable) {
        this.toTable = toTable;
    }

    public String getToColumn() {
        return toColumn;
    }

    public void setToColumn(String toColumn) {
        this.toColumn = toColumn;
    }
    
    public String getJoinType() {
        return joinType;
    }

    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }
}