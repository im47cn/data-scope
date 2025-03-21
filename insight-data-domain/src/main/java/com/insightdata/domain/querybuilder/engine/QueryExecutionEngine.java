package com.insightdata.domain.querybuilder.engine;

/**
 * 查询执行引擎接口
 */
public interface QueryExecutionEngine {

    /**
     * 执行查询
     *
     * @param query          查询语句
     * @param dataSourceId   数据源ID
     * @return               执行结果
     */
    ExecutionResult execute(String query, String dataSourceId);
}