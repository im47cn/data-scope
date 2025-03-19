package com.insightdata.domain.adapter;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.insightdata.domain.exception.DataSourceException;
import com.insightdata.domain.metadata.model.ColumnInfo;
import com.insightdata.domain.metadata.model.DataSource;
import com.insightdata.domain.metadata.model.SchemaInfo;
import com.insightdata.domain.metadata.model.TableInfo;
import com.insightdata.domain.nlquery.executor.QueryResult;

import lombok.extern.slf4j.Slf4j;

/**
 * DB2数据源适配器
 */
@Slf4j
@Component
public class DB2DataSourceAdapter implements DataSourceAdapter {
    
    private Connection connection;
    private DataSource currentDataSource;
    
    @Override
    public void connect(DataSource config) throws Exception {
        try {
            // 加载驱动
            Class.forName(config.getDriverClassName());
            
            // 设置连接属性
            Properties props = new Properties();
            props.setProperty("user", config.getUsername());
            props.setProperty("password", config.getPassword()); // 使用普通密码，非加密
            
            // 添加自定义连接属性
            if (config.getConnectionProperties() != null) {
                config.getConnectionProperties().forEach(props::setProperty);
            }
            
            // 获取连接
            connection = DriverManager.getConnection(config.getJdbcUrl(), props);
            currentDataSource = config;
        } catch (SQLException | ClassNotFoundException e) {
            log.error("Failed to connect to DB2 database: {}", config.getName(), e);
            throw new DataSourceException("Failed to connect to DB2 database: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void disconnect() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connection = null;
        }
    }
    
    @Override
    public boolean testConnection(DataSource config) throws Exception {
        try (Connection conn = getTestConnection(config)) {
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            log.error("Failed to test connection to DB2 database: {}", config.getName(), e);
            throw e;
        }
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
    
    @Override
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * 实现EnhancedDataSourceAdapter接口的方法
     */
    public List<SchemaInfo> getSchemas(DataSource dataSource) throws Exception {
        if (currentDataSource == null || !dataSource.getId().equals(currentDataSource.getId())) {
            connect(dataSource);
        }
        
        List<SchemaInfo> schemas = new ArrayList<>();
        List<String> schemaNames = getSchemas(dataSource.getDatabaseName());
        
        for (String schemaName : schemaNames) {
            SchemaInfo schema = new SchemaInfo();
            schema.setName(schemaName);
            schema.setDataSourceId(dataSource.getId());
            schemas.add(schema);
        }
        
        return schemas;
    }
    
    public SchemaInfo getSchema(DataSource dataSource, String schemaName) throws Exception {
        if (currentDataSource == null || !dataSource.getId().equals(currentDataSource.getId())) {
            connect(dataSource);
        }
        
        SchemaInfo schema = new SchemaInfo();
        schema.setName(schemaName);
        schema.setDataSourceId(dataSource.getId());
        
        // 获取此schema的表信息
        List<TableInfo> tables = getTables(dataSource.getDatabaseName(), schemaName);
        schema.setTables(tables);
        
        return schema;
    }
    
    public List<TableInfo> getTables(DataSource dataSource, String schema) throws Exception {
        if (currentDataSource == null || !dataSource.getId().equals(currentDataSource.getId())) {
            connect(dataSource);
        }
        
        return getTables(dataSource.getDatabaseName(), schema);
    }
    
    // 私有辅助方法
    
    private Connection getTestConnection(DataSource dataSource) throws SQLException, ClassNotFoundException {
        // 加载驱动
        Class.forName(dataSource.getDriverClassName());
        
        // 设置连接属性
        Properties props = new Properties();
        props.setProperty("user", dataSource.getUsername());
        props.setProperty("password", dataSource.getPassword());
        
        // 添加自定义连接属性
        if (dataSource.getConnectionProperties() != null) {
            dataSource.getConnectionProperties().forEach(props::setProperty);
        }
        
        // 获取连接
        return DriverManager.getConnection(dataSource.getJdbcUrl(), props);
    }
    
    private void checkConnection() throws DataSourceException {
        try {
            if (connection == null || connection.isClosed()) {
                throw new DataSourceException("Database connection is not established or has been closed");
            }
        } catch (SQLException e) {
            throw new DataSourceException("Error checking database connection status", e);
        }
    }
    
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
    
    // 以下是辅助方法，用于支持原有功能

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
    
    public String getDatabaseVersion() throws SQLException {
        checkConnection();
        return connection.getMetaData().getDatabaseProductVersion();
    }
    
    public String getDriverVersion() throws SQLException {
        checkConnection();
        return connection.getMetaData().getDriverVersion();
    }
}