package com.insightdata.domain.adapter;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.insightdata.domain.exception.DataSourceException;
import com.insightdata.domain.metadata.model.ColumnInfo;
import com.insightdata.domain.metadata.model.TableInfo;
import com.insightdata.domain.nlquery.executor.QueryResult;

import lombok.extern.slf4j.Slf4j;

/**
 * DB2数据源适配器
 * 
 * 继承AbstractDataSourceAdapter以确保架构一致性
 */
@Slf4j
@Component
public class DB2DataSourceAdapter extends AbstractDataSourceAdapter {
    
    /**
     * 构造函数
     */
    public DB2DataSourceAdapter() {
        // 无需额外初始化
    }
    
    @Override
    public List<String> getCatalogs() throws Exception {
        checkConnection();
        List<String> catalogs = new ArrayList<>();
        try (ResultSet rs = connection.getMetaData().getCatalogs()) {
            while (rs.next()) {
                catalogs.add(rs.getString("TABLE_CAT"));
            }
            return catalogs;
        } catch (SQLException e) {
            log.error("Failed to get catalogs from DB2 database", e);
            throw new DataSourceException("Failed to get catalogs from DB2 database", e);
        }
    }
    
    @Override
    public List<String> getSchemas(String catalog) throws Exception {
        checkConnection();
        List<String> schemas = new ArrayList<>();
        try (ResultSet rs = connection.getMetaData().getSchemas(catalog, null)) {
            while (rs.next()) {
                schemas.add(rs.getString("TABLE_SCHEM"));
            }
            return schemas;
        } catch (SQLException e) {
            log.error("Failed to get schemas from DB2 database", e);
            throw new DataSourceException("Failed to get schemas from DB2 database", e);
        }
    }
    
    @Override
    public List<TableInfo> getTables(String catalog, String schema) throws Exception {
        checkConnection();
        List<TableInfo> tables = new ArrayList<>();
        try (ResultSet rs = connection.getMetaData().getTables(catalog, schema, null, new String[]{"TABLE"})) {
            while (rs.next()) {
                TableInfo table = new TableInfo();
                table.setName(rs.getString("TABLE_NAME"));
                table.setSchemaName(schema);
                table.setDescription(rs.getString("REMARKS"));
                
                // 设置表的统计信息 - 简化实现
                table.setRowCount(getRowCountInternal(schema, table.getName()));
                
                tables.add(table);
            }
            return tables;
        } catch (SQLException e) {
            log.error("Failed to get tables from DB2 database", e);
            throw new DataSourceException("Failed to get tables from DB2 database", e);
        }
    }
    
    @Override
    public List<ColumnInfo> getColumns(String catalog, String schema, String table) throws Exception {
        checkConnection();
        List<ColumnInfo> columns = new ArrayList<>();
        try (ResultSet rs = connection.getMetaData().getColumns(catalog, schema, table, null)) {
            while (rs.next()) {
                ColumnInfo column = ColumnInfo.builder()
                        .name(rs.getString("COLUMN_NAME"))
                        .tableName(table)
                        .dataType(rs.getString("TYPE_NAME"))
                        .isNullable(rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable)
                        .isPrimaryKey(isPrimaryKey(catalog, schema, table, rs.getString("COLUMN_NAME")))
                        .description(rs.getString("REMARKS"))
                        .build();
                
                columns.add(column);
            }
            return columns;
        } catch (SQLException e) {
            log.error("Failed to get columns from DB2 database", e);
            throw new DataSourceException("Failed to get columns from DB2 database", e);
        }
    }
    
    @Override
    public Map<String, Long> getTableSizes(String catalog, String schema) throws Exception {
        checkConnection();
        Map<String, Long> sizes = new HashMap<>();
        
        // 获取该schema下的所有表
        List<TableInfo> tables = getTables(catalog, schema);
        
        // 对每个表获取其大小
        for (TableInfo table : tables) {
            try {
                long size = getDataSizeInternal(schema, table.getName());
                sizes.put(table.getName(), size);
            } catch (Exception e) {
                log.warn("Failed to get size for table {}.{}: {}", schema, table.getName(), e.getMessage());
                // 如果获取失败，仍继续处理其他表
                sizes.put(table.getName(), -1L);
            }
        }
        
        return sizes;
    }
    
    @Override
    public String getDataSourceType() {
        return "DB2";
    }
    
    /**
     * 执行SQL查询并返回结果
     * 
     * @param sql SQL查询语句
     * @return 查询结果
     * @throws SQLException 如果查询执行失败
     */
    public QueryResult executeQuery(String sql) throws SQLException {
        checkConnection();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            // 获取列信息
            List<String> columnLabels = new ArrayList<>();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                columnLabels.add(rs.getMetaData().getColumnLabel(i));
            }
            
            // 获取数据
            List<Map<String, Object>> rows = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (String label : columnLabels) {
                    row.put(label, rs.getObject(label));
                }
                rows.add(row);
            }
            
            return QueryResult.builder()
                    .success(true)
                    .columnLabels(columnLabels)
                    .rows(rows)
                    .build();
        }
    }
    
    /**
     * 获取数据库版本信息
     * 
     * @return 数据库版本字符串
     * @throws SQLException 如果获取失败
     */
    public String getDatabaseVersion() throws SQLException {
        checkConnection();
        return connection.getMetaData().getDatabaseProductVersion();
    }
    
    /**
     * 获取驱动版本信息
     * 
     * @return 驱动版本字符串
     * @throws SQLException 如果获取失败
     */
    public String getDriverVersion() throws SQLException {
        checkConnection();
        return connection.getMetaData().getDriverVersion();
    }
    
    // 私有辅助方法
    
    /**
     * 获取表的行数
     * 
     * @param schemaName 模式名
     * @param tableName 表名
     * @return 表的行数，如果获取失败则返回-1
     */
    private long getRowCountInternal(String schemaName, String tableName) {
        String sql = "SELECT COUNT(*) FROM " + schemaName + "." + tableName;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        } catch (SQLException e) {
            log.warn("Failed to get row count for table {}.{}: {}", schemaName, tableName, e.getMessage());
            return -1;
        }
    }
    
    /**
     * 获取表的数据大小
     * 
     * @param schemaName 模式名
     * @param tableName 表名
     * @return 表的数据大小（字节），如果获取失败则返回-1
     */
    private long getDataSizeInternal(String schemaName, String tableName) {
        String sql = "SELECT SUM(DATA_OBJECT_P_SIZE) FROM TABLE(SYSPROC.ADMIN_GET_TAB_INFO(?, ?))";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, schemaName);
            stmt.setString(2, tableName);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            return 0;
        } catch (SQLException e) {
            log.warn("Failed to get data size for table {}.{}: {}", schemaName, tableName, e.getMessage());
            return -1;
        }
    }
    
    /**
     * 检查列是否为主键
     * 
     * @param catalog 目录名
     * @param schema 模式名
     * @param table 表名
     * @param column 列名
     * @return 如果列是主键则返回true，否则返回false
     */
    private boolean isPrimaryKey(String catalog, String schema, String table, String column) {
        try (ResultSet rs = connection.getMetaData().getPrimaryKeys(catalog, schema, table)) {
            while (rs.next()) {
                if (column.equals(rs.getString("COLUMN_NAME"))) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            log.warn("Failed to check if column is primary key: {}.{}.{}: {}",
                    schema, table, column, e.getMessage());
            return false;
        }
    }
}