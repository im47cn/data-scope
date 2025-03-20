package com.insightdata.domain.datasource.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 外键列信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForeignKeyColumnInfo {
    /**
     * 序号
     */
    private Integer ordinalPosition;

    /**
     * 源列名
     */
    private String sourceColumnName;

    /**
     * 目标列名
     */
    private String targetColumnName;
}