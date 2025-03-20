package com.insightdata.domain.datasource;

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

    /**
     * 表注释
     */
    private String comment;

    /**
     * 列信息列表
     */
    private List<ColumnInfo> columns;
}