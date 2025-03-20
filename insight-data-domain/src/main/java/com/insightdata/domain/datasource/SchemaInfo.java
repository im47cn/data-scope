package com.insightdata.domain.datasource;

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
     * schema下的表信息列表
     */
    private List<TableInfo> tables;
}