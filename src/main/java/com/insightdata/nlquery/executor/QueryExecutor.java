package com.insightdata.nlquery.executor;

import java.util.Map;

import com.insightdata.domain.model.query.QueryResult;

/**
 * 查询执行器接口
 * 负责执行SQL查询并返回结果
 */
public interface QueryExecutor {

    /**
     * 执行SQL查询
     *
     * @param dataSourceId 数据源ID
     * @param sql SQL查询语句
     * @param parameters 查询参数
     * @return 查询结果
     */
    QueryResult executeQuery(Long dataSourceId, String sql, Map<String, Object> parameters);

    /**
     * 测试SQL查询
     * 验证SQL语法是否正确，但不实际执行查询
     *
     * @param dataSourceId 数据源ID
     * @param sql SQL查询语句
     * @return 是否通过验证
     */
    boolean validateQuery(Long dataSourceId, String sql);

    /**
     * 获取查询的元数据
     * 返回查询结果的列信息，但不返回实际数据
     *
     * @param dataSourceId 数据源ID
     * @param sql SQL查询语句
     * @return 查询元数据
     */
    QueryMetadata getQueryMetadata(Long dataSourceId, String sql);
    
    /**
     * 将查询结果导出为CSV格式
     *
     * @param dataSourceId 数据源ID
     * @param sql SQL查询语句
     * @param parameters 查询参数
     * @return 包含CSV数据的字符串
     */
    String exportToCsv(Long dataSourceId, String sql, Map<String, Object> parameters);

    /**
     * 查询元数据
     */
    class QueryMetadata {
        private String[] columnNames;
        private String[] columnTypes;
        private String[] columnLabels;

        public QueryMetadata() {
        }

        public QueryMetadata(String[] columnNames, String[] columnTypes, String[] columnLabels) {
            this.columnNames = columnNames;
            this.columnTypes = columnTypes;
            this.columnLabels = columnLabels;
        }

        public String[] getColumnNames() {
            return columnNames;
        }

        public void setColumnNames(String[] columnNames) {
            this.columnNames = columnNames;
        }

        public String[] getColumnTypes() {
            return columnTypes;
        }

        public void setColumnTypes(String[] columnTypes) {
            this.columnTypes = columnTypes;
        }

        public String[] getColumnLabels() {
            return columnLabels;
        }

        public void setColumnLabels(String[] columnLabels) {
            this.columnLabels = columnLabels;
        }
    }
}