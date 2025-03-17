package com.infrastructure.adapter;

import com.common.exception.DataSourceException;
import com.domain.model.DataSource;
import com.domain.model.metadata.*;
import com.nlquery.executor.QueryResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

/**
 * DB2数据源适配器
 */
@Slf4j
@Component
public class DB2DataSourceAdapter implements DataSourceAdapter {
    
    @Override
    public boolean testConnection(DataSource dataSource) {
        try (Connection conn = getConnection(dataSource)) {
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            log.error("Failed to test connection to DB2 database: {}", dataSource.getName(), e);
            return false;
        }
    }
    
    @Override
    public Connection getConnection(DataSource dataSource) throws SQLException{
        try {
            // 加载驱动
            Class.forName(dataSource.getDriverClassName());
            
            // 设置连接属性
            Properties props = new Properties();
            props.setProperty("user", dataSource.getUsername());
            props.setProperty("password", dataSource.getEncryptedPassword()); // 注意：这里应该使用解密后的密码
            
            // 添加自定义连接属性
            if (dataSource.getConnectionProperties() != null) {
                dataSource.getConnectionProperties().forEach(props::setProperty);
            }
            
            // 获取连接
            return DriverManager.getConnection(dataSource.getJdbcUrl(), props);
        } catch (SQLException | ClassNotFoundException e) {
            log.error("Failed to connect to DB2 database: {}", dataSource.getName(), e);
            throw DataSourceException.connectionError("Failed to connect to DB2 database: " + e.getMessage(), e);
        }
    }
    
    @Override
    public SchemaInfo getSchema(DataSource dataSource, String schemaName) {
        try (Connection conn = getConnection(dataSource)) {
            DatabaseMetaData metaData = conn.getMetaData();
            
            try (ResultSet rs = metaData.getSchemas(null, schemaName)) {
                if (rs.next()) {
                    return SchemaInfo.builder()
                            .name(schemaName)
                            .dataSourceId(dataSource.getId())
                            .build();
                }
            }
            
            return null;
            
        } catch (SQLException e) {
            log.error("Failed to get schema from DB2 database: {}", dataSource.getName(), e);
            throw new RuntimeException("Failed to get schema from DB2 database", e);
        }
    }
    
    @Override
    public TableInfo getTable(DataSource dataSource, String schemaName, String tableName) {
        try (Connection conn = getConnection(dataSource)) {
            DatabaseMetaData metaData = conn.getMetaData();
            
            try (ResultSet rs = metaData.getTables(null, schemaName, tableName, new String[]{"TABLE"})) {
                if (rs.next()) {
                    TableInfo table = TableInfo.builder()
                            .name(tableName)
                            .type(rs.getString("TABLE_TYPE"))
                            .remarks(rs.getString("REMARKS"))
                            .schemaName(schemaName)
                            .dataSourceId(dataSource.getId())
                            .build();
                    
                    // 设置表的统计信息
                    table.setRowCount(getRowCount(dataSource, schemaName, tableName));
                    table.setDataSize(getDataSize(dataSource, schemaName, tableName));
                    table.setIndexSize(getIndexSize(dataSource, schemaName, tableName));
                    
                    return table;
                }
            }
            
            return null;
            
        } catch (SQLException e) {
            log.error("Failed to get table from DB2 database: {}", dataSource.getName(), e);
            throw new RuntimeException("Failed to get table from DB2 database", e);
        }
    }
    
    @Override
    public long getRowCount(DataSource dataSource, String schemaName, String tableName) {
        String sql = "SELECT COUNT(*) FROM " + schemaName + "." + tableName;
        
        try (Connection conn = getConnection(dataSource);
             PreparedStatement stmt = conn.prepareStatement(sql);
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
    
    @Override
    public List<SchemaInfo> getSchemas(DataSource dataSource) {
        try (Connection conn = getConnection(dataSource)) {
            DatabaseMetaData metaData = conn.getMetaData();
            List<SchemaInfo> schemas = new ArrayList<>();
            
            try (ResultSet rs = metaData.getSchemas()) {
                while (rs.next()) {
                    SchemaInfo schema = SchemaInfo.builder()
                            .name(rs.getString("TABLE_SCHEM"))
                            .dataSourceId(dataSource.getId())
                            .build();
                    schemas.add(schema);
                }
            }
            
            return schemas;
            
        } catch (SQLException e) {
            log.error("Failed to get schemas from DB2 database: {}", dataSource.getName(), e);
            throw new RuntimeException("Failed to get schemas from DB2 database", e);
        }
    }
    
    @Override
    public List<TableInfo> getTables(DataSource dataSource, String schemaName) {
        try (Connection conn = getConnection(dataSource)) {
            DatabaseMetaData metaData = conn.getMetaData();
            List<TableInfo> tables = new ArrayList<>();
            
            try (ResultSet rs = metaData.getTables(null, schemaName, null, new String[]{"TABLE"})) {
                while (rs.next()) {
                    TableInfo table = TableInfo.builder()
                            .name(rs.getString("TABLE_NAME"))
                            .type(rs.getString("TABLE_TYPE"))
                            .remarks(rs.getString("REMARKS"))
                            .schemaName(schemaName)
                            .dataSourceId(dataSource.getId())
                            .build();
                    
                    // 设置表的统计信息
                    table.setRowCount(getRowCount(dataSource, schemaName, table.getName()));
//                    table.setDataSize(getDataSize(dataSource, schemaName, table.getName()));
//                    table.setIndexSize(getIndexSize(dataSource, schemaName, table.getName()));
                    
                    tables.add(table);
                }
            }
            
            return tables;
            
        } catch (SQLException e) {
            log.error("Failed to get tables from DB2 database: {}", dataSource.getName(), e);
            throw new RuntimeException("Failed to get tables from DB2 database", e);
        }
    }
    
    @Override
    public List<ColumnInfo> getColumns(DataSource dataSource, String schemaName, String tableName) {
        try (Connection conn = getConnection(dataSource)) {
            DatabaseMetaData metaData = conn.getMetaData();
            List<ColumnInfo> columns = new ArrayList<>();
            
            try (ResultSet rs = metaData.getColumns(null, schemaName, tableName, null)) {
                while (rs.next()) {
                    ColumnInfo column = ColumnInfo.builder()
                            .name(rs.getString("COLUMN_NAME"))
                            .dataType(rs.getString("TYPE_NAME"))
                            .length(rs.getInt("COLUMN_SIZE"))
                            .numericPrecision(rs.getInt("COLUMN_SIZE"))
                            .numericScale(rs.getInt("DECIMAL_DIGITS"))
                            .nullable(rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable)
                            .defaultValue(rs.getString("COLUMN_DEF"))
                            .remarks(rs.getString("REMARKS"))
                            .ordinalPosition(rs.getInt("ORDINAL_POSITION"))
                            .autoIncrement(isAutoIncrement(dataSource, schemaName, tableName, rs.getString("COLUMN_NAME")))
                            .build();
                    
                    columns.add(column);
                }
            }
            
            return columns;
            
        } catch (SQLException e) {
            log.error("Failed to get columns from DB2 database: {}", dataSource.getName(), e);
            throw new RuntimeException("Failed to get columns from DB2 database", e);
        }
    }
    
    @Override
    public List<IndexInfo> getIndexes(DataSource dataSource, String schemaName, String tableName) {
        try (Connection conn = getConnection(dataSource)) {
            DatabaseMetaData metaData = conn.getMetaData();
            List<IndexInfo> indexes = new ArrayList<>();
            IndexInfo currentIndex = null;
            
            try (ResultSet rs = metaData.getIndexInfo(null, schemaName, tableName, false, false)) {
                while (rs.next()) {
                    String indexName = rs.getString("INDEX_NAME");
                    if (indexName == null) {
                        continue;
                    }
                    
                    // 如果是新索引，创建新的IndexInfo对象
                    if (currentIndex == null || !indexName.equals(currentIndex.getName())) {
                        currentIndex = IndexInfo.builder()
                                .name(indexName)
                                .unique(!rs.getBoolean("NON_UNIQUE"))
                                .type(rs.getShort("TYPE") == DatabaseMetaData.tableIndexClustered ? "CLUSTERED" : "NONCLUSTERED")
                                .build();
                        indexes.add(currentIndex);
                    }
                    
                    // 添加索引列
                    IndexColumnInfo indexColumn = IndexColumnInfo.builder()
                            .columnName(rs.getString("COLUMN_NAME"))
                            .ordinalPosition(rs.getInt("ORDINAL_POSITION"))
                            .ascending(rs.getString("ASC_OR_DESC").equals("A"))
                            .build();
                    
                    currentIndex.getColumns().add(indexColumn);
                }
            }
            
            return indexes;
            
        } catch (SQLException e) {
            log.error("Failed to get indexes from DB2 database: {}", dataSource.getName(), e);
            throw new RuntimeException("Failed to get indexes from DB2 database", e);
        }
    }
    
    @Override
    public List<ForeignKeyInfo> getForeignKeys(DataSource dataSource, String schemaName, String tableName) {
        try (Connection conn = getConnection(dataSource)) {
            DatabaseMetaData metaData = conn.getMetaData();
            List<ForeignKeyInfo> foreignKeys = new ArrayList<>();
            ForeignKeyInfo currentFK = null;
            
            try (ResultSet rs = metaData.getImportedKeys(null, schemaName, tableName)) {
                while (rs.next()) {
                    String fkName = rs.getString("FK_NAME");
                    
                    // 如果是新外键，创建新的ForeignKeyInfo对象
                    if (currentFK == null || !fkName.equals(currentFK.getName())) {
                        currentFK = ForeignKeyInfo.builder()
                                .name(fkName)
                                .sourceTableName(rs.getString("FKTABLE_NAME"))
                                .targetTableName(rs.getString("PKTABLE_NAME"))
                                .updateRule(rs.getString("UPDATE_RULE"))
                                .deleteRule(rs.getString("DELETE_RULE"))
                                .deferrability(rs.getString("DEFERRABILITY"))
                                .build();
                        foreignKeys.add(currentFK);
                    }
                    
                    // 添加外键列
                    ForeignKeyColumnInfo foreignKeyColumn = ForeignKeyColumnInfo.builder()
                            .sourceColumnName(rs.getString("FKCOLUMN_NAME"))
                            .targetColumnName(rs.getString("PKCOLUMN_NAME"))
                            .ordinalPosition(rs.getInt("KEY_SEQ"))
                            .build();
                    
                    currentFK.getColumns().add(foreignKeyColumn);
                }
            }
            
            return foreignKeys;
            
        } catch (SQLException e) {
            log.error("Failed to get foreign keys from DB2 database: {}", dataSource.getName(), e);
            throw new RuntimeException("Failed to get foreign keys from DB2 database", e);
        }
    }
    
    @Override
    public QueryResult executeQuery(String sql, DataSource dataSource) {
        try (Connection conn = getConnection(dataSource);
             PreparedStatement stmt = conn.prepareStatement(sql);
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
            
        } catch (SQLException e) {
            log.error("Failed to execute query on DB2 database: {}", dataSource.getName(), e);
            throw new RuntimeException("Failed to execute query on DB2 database", e);
        }
    }
    
    @Override
    public long getDataSize(DataSource dataSource, String schemaName, String tableName) {
        String sql = "SELECT SUM(DATA_OBJECT_P_SIZE) FROM TABLE(SYSPROC.ADMIN_GET_TAB_INFO(?, ?))";
        
        try (Connection conn = getConnection(dataSource);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
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
    
    @Override
    public long getIndexSize(DataSource dataSource, String schemaName, String tableName) {
        String sql = "SELECT SUM(INDEX_OBJECT_P_SIZE) FROM TABLE(SYSPROC.ADMIN_GET_TAB_INFO(?, ?))";
        
        try (Connection conn = getConnection(dataSource);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, schemaName);
            stmt.setString(2, tableName);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            
            return 0;
            
        } catch (SQLException e) {
            log.warn("Failed to get index size for table {}.{}: {}", schemaName, tableName, e.getMessage());
            return -1;
        }
    }
    
    @Override
    public boolean isAutoIncrement(DataSource dataSource, String schemaName, String tableName, String columnName) {
        String sql = "SELECT IDENTITY FROM SYSCAT.COLUMNS WHERE TABSCHEMA = ? AND TABNAME = ? AND COLNAME = ?";
        
        try (Connection conn = getConnection(dataSource);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, schemaName);
            stmt.setString(2, tableName);
            stmt.setString(3, columnName);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getString(1) != null;
            }
            
        } catch (SQLException e) {
            log.warn("Failed to check if column is auto increment: {}.{}.{}: {}",
                    schemaName, tableName, columnName, e.getMessage());
            return false;
        }
    }
    
    @Override
    public String getDatabaseVersion(DataSource dataSource) {
        try (Connection conn = getConnection(dataSource)) {
            return conn.getMetaData().getDatabaseProductVersion();
        } catch (SQLException e) {
            return "Unknown";
        }
    }
    
    @Override
    public String getDriverVersion(DataSource dataSource) {
        try (Connection conn = getConnection(dataSource)) {
            return conn.getMetaData().getDriverVersion();
        } catch (SQLException e) {
            return "Unknown";
        }
    }
    
    @Override
    public String getDefaultSchema(DataSource dataSource) {
        try (Connection conn = getConnection(dataSource)) {
            return conn.getSchema();
        } catch (SQLException e) {
            return null;
        }
    }
    
    @Override
    public List<String> getSystemSchemas(DataSource dataSource) {
        return Arrays.asList("SYSCAT", "SYSIBM", "SYSIBMADM", "SYSPUBLIC", "SYSSTAT", "SYSTOOLS");
    }
    
    @Override
    public boolean isSystemSchema(DataSource dataSource, String schemaName) {
        return getSystemSchemas(dataSource).contains(schemaName.toUpperCase());
    }
    
    @Override
    public boolean isSystemTable(DataSource dataSource, String schemaName, String tableName) {
        return isSystemSchema(dataSource, schemaName) || tableName.startsWith("SYS");
    }
}