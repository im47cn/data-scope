package com.insightdata.domain.datasource.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库表信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableInfo {
    /**
     * 表名
     */
    private String name;

    /**
     * schema名称
     */
    private String schemaName;

    private long rowCount;

    private long dataSize;

    private long indexSize;

    /**
     * 表注释
     */
    private String comment;

    /**
     * 列信息列表
     */
    private List<ColumnInfo> columns;

    private List<ForeignKeyInfo> foreignKeys;

    private String description;

}