package com.insightdata.domain.datasource.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库列信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnInfo {

    /**
     * 列名
     */
    private String name;

    /**
     * 列类型
     */
    private String type;

    /**
     * 是否可为空
     */
    private boolean nullable;

    /**
     * 是否为主键
     */
    private boolean primaryKey;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 列注释
     */
    private String comment;

    /**
     * 字符集
     */
    private String charset;

    /**
     * 排序规则
     */
    private String collation;

    /**
     * 列长度
     */
    private Long length;

    /**
     * 小数点位数
     */
    private Integer scale;

    /**
     * 是否自增
     */
    private boolean autoIncrement;

    /**
     * 外键信息
     */
    private List<ForeignKeyInfo> foreignKeys;
}