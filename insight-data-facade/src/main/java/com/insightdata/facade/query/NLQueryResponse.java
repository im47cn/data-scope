package com.insightdata.facade.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 自然语言查询响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NLQueryResponse {

    /**
     * 查询ID
     */
    private String queryId;

    /**
     * 原始查询
     */
    private String originalQuery;

    /**
     * 生成的SQL
     */
    private String generatedSql;

    /**
     * 查询结果数据
     */
    private List<Map<String, Object>> data;

    /**
     * 执行时间（毫秒）
     */
    private Long executionTime;

    /**
     * 总行数
     */
    private Long totalRows;

    /**
     * 返回行数
     */
    private Integer returnedRows;

    /**
     * 查询状态（成功/失败）
     */
    private String status;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 查询解释
     */
    private List<String> explanations;
}