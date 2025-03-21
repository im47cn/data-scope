package com.insightdata.domain.querybuilder.engine;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

/**
 * 查询执行结果
 */
@Data
@Builder
public class ExecutionResult {

    /**
     * 查询ID
     */
    private String queryId;

    /**
     * 列标签
     */
    private List<String> columnLabels;

    /**
     * 行数据
     */
    private List<Map<String, Object>> rows;

    /**
     * 执行状态
     */
    private ExecutionStatus status;

    /**
     * 执行统计信息
     */
    private ExecutionStats stats;

    /**
     * 是否执行成功
     */
    private boolean success;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 执行时长
     */
    private long duration;

    /**
     * 总行数
     */
    private int totalRows;
}