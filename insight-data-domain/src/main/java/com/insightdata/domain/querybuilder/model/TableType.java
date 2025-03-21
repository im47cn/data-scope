package com.insightdata.domain.querybuilder.model;

/**
 * Enumeration of table types
 */
public enum TableType {
    BASE_TABLE,
    VIEW,
    MATERIALIZED_VIEW,
    SYSTEM_TABLE,
    TEMPORARY_TABLE,
    ALIAS,
    SUBQUERY,
    UNKNOWN
}