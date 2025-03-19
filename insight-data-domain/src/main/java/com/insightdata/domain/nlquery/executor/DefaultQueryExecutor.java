package com.insightdata.domain.nlquery.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.insightdata.domain.adapter.DataSourceAdapter;
import com.insightdata.domain.adapter.DataSourceAdapterFactory;
import com.insightdata.domain.adapter.DataSourceAdapterHelper;
import com.insightdata.domain.metadata.model.DataSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认查询执行器实现
 * 
 * 基于JDBC执行SQL查询，支持多种数据源类型
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultQueryExecutor implements QueryExecutor {

    private final DataSourceAdapterFactory dataSourceAdapterFactory;
    
    // 存储查询状态的映射
    private final Map<String, QueryStatus> queryStatusMap = new ConcurrentHashMap<>();
    
    // 存储可取消的语句的映射
    private final Map<String, PreparedStatement> statementMap = new ConcurrentHashMap<>();

    @Override
    public QueryResult execute(String sql, DataSource dataSource) {
        return execute(sql, dataSource, null);
    }

    @Override
    public QueryResult execute(String sql, DataSource dataSource, QueryMetadata metadata) {
        String queryId = generateQueryId();
        queryStatusMap.put(queryId, QueryStatus.RUNNING);
        
        long startTime = System.currentTimeMillis();
        
        try {
            // 使用自定义的Helper类处理IDE无法识别Lombok生成的getter方法的问题
            if (sql == null || sql.trim().isEmpty()) {
                queryStatusMap.put(queryId, QueryStatus.FAILED);
                return QueryResult.builder()
                        .success(false)
                        .errorMessage("SQL语句不能为空")
                        .queryId(queryId)
                        .build();
            }
            
            // 获取适配的数据源适配器
            DataSourceAdapter adapter = dataSourceAdapterFactory.getAdapter(DataSourceAdapterHelper.getType(dataSource));
            
            // 执行查询并获取结果
            Connection connection = adapter.getConnection();
            if (connection == null) {
                adapter.connect(dataSource);
                connection = adapter.getConnection();
            }
            
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statementMap.put(queryId, statement);
                
                ResultSet resultSet = statement.executeQuery();
                ResultSetMetaData rsMetaData = resultSet.getMetaData();
                int columnCount = rsMetaData.getColumnCount();
                
                // 提取列信息
                List<String> columnLabels = new ArrayList<>();
                List<String> columnTypes = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    columnLabels.add(rsMetaData.getColumnLabel(i));
                    columnTypes.add(rsMetaData.getColumnTypeName(i));
                }
                
                // 提取数据行
                List<Map<String, Object>> rows = new ArrayList<>();
                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = rsMetaData.getColumnLabel(i);
                        Object value = resultSet.getObject(i);
                        row.put(columnName, value);
                    }
                    rows.add(row);
                }
                
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                
                // 清理资源
                resultSet.close();
                statement.close();
                statementMap.remove(queryId);
                
                // 更新状态
                queryStatusMap.put(queryId, QueryStatus.COMPLETED);
                
                // 构建并返回结果
                return QueryResult.builder()
                        .success(true)
                        .duration(duration)
                        .columnLabels(columnLabels)
                        .columnTypes(columnTypes)
                        .rows(rows)
                        .affectedRows(rows.size())
                        .totalRows(rows.size())
                        .queryId(queryId)
                        .build();
                
            } catch (SQLException e) {
                queryStatusMap.put(queryId, QueryStatus.FAILED);
                return QueryResult.builder()
                        .success(false)
                        .errorMessage("SQL执行错误: " + e.getMessage())
                        .queryId(queryId)
                        .build();
            } finally {
                statementMap.remove(queryId);
            }
            
        } catch (Exception e) {
            queryStatusMap.put(queryId, QueryStatus.FAILED);
            return QueryResult.builder()
                    .success(false)
                    .errorMessage("查询执行错误: " + e.getMessage())
                    .queryId(queryId)
                    .build();
        }
    }
    
    @Override
    public QueryStatus getStatus(String queryId) {
        return queryStatusMap.getOrDefault(queryId, QueryStatus.UNKNOWN);
    }
    
    @Override
    public boolean cancel(String queryId) {
        try {
            PreparedStatement statement = statementMap.get(queryId);
            if (statement != null) {
                statement.cancel();
                statementMap.remove(queryId);
                queryStatusMap.put(queryId, QueryStatus.CANCELLED);
                return true;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * 生成唯一的查询ID
     */
    private String generateQueryId() {
        return "query-" + System.currentTimeMillis() + "-" + Math.round(Math.random() * 10000);
    }
}