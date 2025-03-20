package com.insightdata.domain.datasource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 存储过程信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcedureInfo {
    /**
     * 存储过程名称
     */
    private String name;

    /**
     * 所属模式
     */
    private String schema;

    /**
     * 存储过程类型(PROCEDURE/FUNCTION)
     */
    private String type;

    /**
     * 参数列表
     */
    private Parameter[] parameters;

    /**
     * 返回类型
     */
    private String returnType;

    /**
     * 存储过程定义
     */
    private String definition;

    /**
     * 是否确定性的
     */
    private boolean deterministic;

    /**
     * 数据访问类型(CONTAINS SQL/NO SQL/READS SQL DATA/MODIFIES SQL DATA)
     */
    private String dataAccess;

    /**
     * SQL安全性(DEFINER/INVOKER)
     */
    private String sqlSecurity;

    /**
     * 注释
     */
    private String comment;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 存储过程参数信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Parameter {
        /**
         * 参数名称
         */
        private String name;

        /**
         * 参数模式(IN/OUT/INOUT)
         */
        private String mode;

        /**
         * 参数类型
         */
        private String type;

        /**
         * 参数长度
         */
        private Integer length;

        /**
         * 参数精度
         */
        private Integer precision;

        /**
         * 参数小数位数
         */
        private Integer scale;

        /**
         * 参数默认值
         */
        private String defaultValue;

        /**
         * 参数注释
         */
        private String comment;
    }
}