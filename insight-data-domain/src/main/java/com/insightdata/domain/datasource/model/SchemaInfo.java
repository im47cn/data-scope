package com.insightdata.domain.datasource.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库schema信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchemaInfo {
    /**
     * schema名称
     */
    private String name;

    /**
     * 数据源ID
     */
    private String dataSourceId;

    /**
     * schema下的表信息列表
     */
    private List<TableInfo> tables;

    /**
     * 是否是默认schema
     */
    private Boolean isDefault;

    /**
     * schema描述
     */
    private String description;

    /**
     * 表数量
     */
    private Integer tableCount;

    /**
     * 创建时间
     */
    private String createTime;
}