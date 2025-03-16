package com.insightdata.nlquery.executor;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.sql.DataSource;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.insightdata.common.exception.QueryTimeoutException;
import com.insightdata.domain.model.query.QueryResult;
import com.insightdata.domain.service.DataSourceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 查询执行器的默认实现
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultQueryExecutor implements QueryExecutor {

    private final DataSourceService dataSourceService;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    
    @Value("${query.timeout.seconds:30}")
    private int queryTimeoutSeconds = 30;
    
    @Value("${query.max.rows:50000}")
    private int maxRows = 50000;

    @Override
    public QueryResult executeQuery(Long dataSourceId, String sql, Map<String, Object> parameters) {
        QueryResult result = new QueryResult();
        final long startTime = System.currentTimeMillis();
        
        Future<Boolean> future = executorService.submit(() -> {
            try (Connection connection = getConnection(dataSourceId);
                 PreparedStatement statement = prepareStatement(connection, sql, parameters)) {
                
                // 设置查询超时
                statement.setQueryTimeout(queryTimeoutSeconds);
                // 限制查询结果条数
                statement.setMaxRows(maxRows);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    // 获取结果集元数据
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    
                    // 获取列名
                    List<String> columns = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        columns.add(metaData.getColumnLabel(i));
                    }
                    result.setColumns(columns);
                    
                    // 获取数据
                    List<Map<String, Object>> data = new ArrayList<>();
                    while (resultSet.next()) {
                        Map<String, Object> row = new HashMap<>();
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = metaData.getColumnLabel(i);
                            Object value = resultSet.getObject(i);
                            row.put(columnName, value);
                        }
                        data.add(row);
                    }
                    result.setData(data);
                    
                    // 设置成功标志
                    result.setSuccess(true);
                }
                return true;
            } catch (SQLException e) {
                log.error("执行查询时发生错误", e);
                result.setSuccess(false);
                result.setErrorMessage(e.getMessage());
                return false;
            }
        });
        
        try {
            future.get(queryTimeoutSeconds, TimeUnit.SECONDS);
            
            // 设置执行时间
            final long endTime = System.currentTimeMillis();
            result.setExecutionTime(endTime - startTime);
            
        } catch (TimeoutException e) {
            log.error("查询执行超时", e);
            future.cancel(true);
            result.setSuccess(false);
            result.setErrorMessage("查询执行超时，已超过" + queryTimeoutSeconds + "秒");
            throw new QueryTimeoutException("查询执行超时，已超过" + queryTimeoutSeconds + "秒");
        } catch (Exception e) {
            log.error("查询执行过程中发生异常", e);
            future.cancel(true);
            result.setSuccess(false);
            result.setErrorMessage("查询执行失败: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    public boolean validateQuery(Long dataSourceId, String sql) {
        try (Connection connection = getConnection(dataSourceId);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            // 只验证SQL语法，不实际执行查询
            statement.setFetchSize(0);
            statement.setMaxRows(0);
            statement.getMetaData();
            
            return true;
        } catch (SQLException e) {
            log.error("验证查询时发生错误", e);
            return false;
        }
    }

    @Override
    public QueryMetadata getQueryMetadata(Long dataSourceId, String sql) {
        QueryMetadata metadata = new QueryMetadata();
        
        try (Connection connection = getConnection(dataSourceId);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            // 获取结果集元数据
            ResultSetMetaData resultSetMetaData = statement.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            
            // 设置列信息
            String[] columnNames = new String[columnCount];
            String[] columnTypes = new String[columnCount];
            String[] columnLabels = new String[columnCount];
            
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = resultSetMetaData.getColumnName(i);
                columnTypes[i - 1] = resultSetMetaData.getColumnTypeName(i);
                columnLabels[i - 1] = resultSetMetaData.getColumnLabel(i);
            }
            
            metadata.setColumnNames(columnNames);
            metadata.setColumnTypes(columnTypes);
            metadata.setColumnLabels(columnLabels);
            
        } catch (SQLException e) {
            log.error("获取查询元数据时发生错误", e);
        }
        
        return metadata;
    }
    
    @Override
    public String exportToCsv(Long dataSourceId, String sql, Map<String, Object> parameters) {
        StringWriter writer = new StringWriter();
        
        try (Connection connection = getConnection(dataSourceId);
             PreparedStatement statement = prepareStatement(connection, sql, parameters)) {
             
            // 设置查询超时和最大行数限制
            statement.setQueryTimeout(queryTimeoutSeconds);
            statement.setMaxRows(maxRows);
             
            try (ResultSet resultSet = statement.executeQuery();
                 CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(resultSet))) {
                
                // 自动添加了头，现在只需要添加数据
                while (resultSet.next()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    
                    List<Object> rowData = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        rowData.add(resultSet.getObject(i));
                    }
                    csvPrinter.printRecord(rowData);
                }
                
            } catch (IOException e) {
                log.error("生成CSV时发生错误", e);
                throw new SQLException("生成CSV时发生错误: " + e.getMessage());
            }
            
            return writer.toString();
        } catch (SQLException e) {
            log.error("导出CSV时发生SQL错误", e);
            throw new RuntimeException("导出CSV时发生错误: " + e.getMessage());
        }
    }
    
    /**
     * 获取数据源连接
     */
    private Connection getConnection(Long dataSourceId) throws SQLException {
        DataSource dataSource = dataSourceService.getDataSource(dataSourceId);
        return dataSource.getConnection();
    }
    
    /**
     * 准备SQL语句
     */
    private PreparedStatement prepareStatement(Connection connection, String sql, Map<String, Object> parameters) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql,
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        
        // 设置较大的批处理获取大小以提高性能
        statement.setFetchSize(1000);
        
        if (parameters != null && !parameters.isEmpty()) {
            // 设置参数
            int paramIndex = 1;
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                statement.setObject(paramIndex++, entry.getValue());
            }
        }
        
        return statement;
    }
}