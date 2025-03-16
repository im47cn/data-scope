package com.insightdata.nlquery.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

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

    @Override
    public QueryResult executeQuery(Long dataSourceId, String sql, Map<String, Object> parameters) {
        QueryResult result = new QueryResult();
        
        try (Connection connection = getConnection(dataSourceId);
             PreparedStatement statement = prepareStatement(connection, sql, parameters);
             ResultSet resultSet = statement.executeQuery()) {
            
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
            
        } catch (SQLException e) {
            log.error("执行查询时发生错误", e);
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
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
        PreparedStatement statement = connection.prepareStatement(sql);
        
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