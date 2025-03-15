package com.insightdata.infrastructure.adapter;

import com.insightdata.common.enums.DataSourceType;
import com.insightdata.common.exception.DataSourceException;
import com.insightdata.domain.adapter.DataSourceAdapter;
import com.insightdata.domain.model.DataSource;
import com.insightdata.domain.model.metadata.ColumnInfo;
import com.insightdata.domain.model.metadata.ForeignKeyColumnInfo;
import com.insightdata.domain.model.metadata.ForeignKeyInfo;
import com.insightdata.domain.model.metadata.IndexColumnInfo;
import com.insightdata.domain.model.metadata.IndexInfo;
import com.insightdata.domain.model.metadata.SchemaInfo;
import com.insightdata.domain.model.metadata.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * DB2数据源适配器实现
 */
@Slf4j
@Component
public class DB2DataSourceAdapter implements DataSourceAdapter {

    private static final String TYPE = DataSourceType.DB2.name();
    
    @Override
    public String getType() {
        return TYPE;
    }
    
    @Override
    public boolean testConnection(DataSource dataSource) {
        try (Connection connection = getConnection(dataSource)) {
            return connection != null && connection.isValid(5);
        } catch (Exception e) {
            log.error("Failed to test connection to DB2 database: {}", dataSource.getName(), e);
            return false;
        }
    }
    
    @Override
    public Connection getConnection(DataSource dataSource) {
        try {
            // 加载驱动
            Class.forName(dataSource.getDriverClassName());
            
            // 构建连接属性
            Properties props = new Properties();
            props.setProperty("user", dataSource.getUsername());
            props.setProperty("password", dataSource.getEncryptedPassword()); // 注意：这里应该使用解密后的密码
            
            // 添加其他连接属性
            if (dataSource.getConnectionProperties() != null) {
                dataSource.getConnectionProperties().forEach(props::setProperty);
            }
            
            // 获取连接
            return DriverManager.getConnection(dataSource.getJdbcUrl(), props);
        } catch (ClassNotFoundException e) {
            log.error("DB2 JDBC driver not found", e);
            throw DataSourceException.connectionError("DB2 JDBC driver not found: " + e.getMessage(), e);
        } catch (SQLException e) {
            log.error("Failed to connect to DB2 database: {}", dataSource.getName(), e);
            throw DataSourceException.connectionError("Failed to connect to DB2 database: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<SchemaInfo> getSchemas(DataSource dataSource) {
        List<SchemaInfo> schemas = new ArrayList<>();
        
        try (Connection connection = getConnection(dataSource)) {
            // 获取所有模式
            try (ResultSet rs = connection.getMetaData().getSchemas()) {
                while (rs.next()) {
                    String schemaName = rs.getString("TABLE_SCHEM");
                    
                    // 排除系统模式
                    if (isSystemSchema(schemaName)) {
                        continue;
                    }
                    
                    SchemaInfo schema = SchemaInfo.builder()
                            .dataSourceId(dataSource.getId())
                            .name(schemaName)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    
                    schemas.add(schema);
                }
            }
        } catch (SQLException e) {
            log.error("Failed to get schemas from DB2 database: {}", dataSource.getName(), e);
            throw DataSourceException.metadataExtractionError("Failed to get schemas: " + e.getMessage(), e);
        }
        
        return schemas;
    }
    
    @Override
    public List<TableInfo> getTables(DataSource dataSource, String schemaName) {
        List<TableInfo> tables = new ArrayList<>();
        
        try (Connection connection = getConnection(dataSource)) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            try (ResultSet rs = metaData.getTables(null, schemaName, "%", new String[]{"TABLE", "VIEW"})) {
                while (rs.next()) {
                    String tableName = rs.getString("TABLE_NAME");
                    String tableType = rs.getString("TABLE_TYPE");
                    String remarks = rs.getString("REMARKS");
                    
                    TableInfo table = TableInfo.builder()
                            .schemaId(null) // 需要在保存时设置
                            .name(tableName)
                            .type(tableType)
                            .description(remarks)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    
                    tables.add(table);
                }
            }
            
            // 获取表的行数和大小信息
            for (TableInfo table : tables) {
                table.setEstimatedRowCount(getEstimatedRowCount(dataSource, schemaName, table.getName()));
                table.setDataSize(getDataSize(dataSource, schemaName, table.getName()));
                table.setIndexSize(getIndexSize(dataSource, schemaName, table.getName()));
            }
        } catch (SQLException e) {
            log.error("Failed to get tables from DB2 database: {}", dataSource.getName(), e);
            throw DataSourceException.metadataExtractionError("Failed to get tables: " + e.getMessage(), e);
        }
        
        return tables;
    }
    
    @Override
    public List<ColumnInfo> getColumns(DataSource dataSource, String schemaName, String tableName) {
        List<ColumnInfo> columns = new ArrayList<>();
        
        try (Connection connection = getConnection(dataSource)) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            // 获取主键信息
            Map<String, Boolean> primaryKeys = new HashMap<>();
            try (ResultSet pkRs = metaData.getPrimaryKeys(null, schemaName, tableName)) {
                while (pkRs.next()) {
                    String columnName = pkRs.getString("COLUMN_NAME");
                    primaryKeys.put(columnName, true);
                }
            }
            
            // 获取列信息
            try (ResultSet rs = metaData.getColumns(null, schemaName, tableName, "%")) {
                while (rs.next()) {
                    String columnName = rs.getString("COLUMN_NAME");
                    String dataType = rs.getString("TYPE_NAME");
                    String columnType = dataType;
                    int ordinalPosition = rs.getInt("ORDINAL_POSITION");
                    int columnSize = rs.getInt("COLUMN_SIZE");
                    int decimalDigits = rs.getInt("DECIMAL_DIGITS");
                    boolean nullable = rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable;
                    String defaultValue = rs.getString("COLUMN_DEF");
                    String remarks = rs.getString("REMARKS");
                    
                    // DB2不直接提供自增列信息，需要通过其他方式获取
                    boolean isAutoIncrement = isAutoIncrementColumn(connection, schemaName, tableName, columnName);
                    
                    // 构建列类型（包含长度、精度等信息）
                    if (columnSize > 0) {
                        if (decimalDigits > 0) {
                            columnType = dataType + "(" + columnSize + "," + decimalDigits + ")";
                        } else {
                            columnType = dataType + "(" + columnSize + ")";
                        }
                    }
                    
                    ColumnInfo column = ColumnInfo.builder()
                            .tableId(null) // 需要在保存时设置
                            .name(columnName)
                            .dataType(dataType)
                            .columnType(columnType)
                            .ordinalPosition(ordinalPosition)
                            .length(columnSize)
                            .precision(columnSize)
                            .scale(decimalDigits)
                            .nullable(nullable)
                            .defaultValue(defaultValue)
                            .description(remarks)
                            .isPrimaryKey(primaryKeys.containsKey(columnName))
                            .isForeignKey(false) // 将在获取外键信息时更新
                            .isAutoIncrement(isAutoIncrement)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    
                    columns.add(column);
                }
            }
        } catch (SQLException e) {
            log.error("Failed to get columns from DB2 database: {}", dataSource.getName(), e);
            throw DataSourceException.metadataExtractionError("Failed to get columns: " + e.getMessage(), e);
        }
        
        return columns;
    }
    
    @Override
    public List<IndexInfo> getIndexes(DataSource dataSource, String schemaName, String tableName) {
        Map<String, IndexInfo> indexMap = new HashMap<>();
        
        try (Connection connection = getConnection(dataSource)) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            try (ResultSet rs = metaData.getIndexInfo(null, schemaName, tableName, false, true)) {
                while (rs.next()) {
                    String indexName = rs.getString("INDEX_NAME");
                    
                    // 跳过主键索引
                    if (indexName == null || isPrimaryKeyIndex(indexName)) {
                        continue;
                    }
                    
                    boolean nonUnique = rs.getBoolean("NON_UNIQUE");
                    String columnName = rs.getString("COLUMN_NAME");
                    int ordinalPosition = rs.getInt("ORDINAL_POSITION");
                    String ascOrDesc = rs.getString("ASC_OR_DESC");
                    
                    // 获取或创建索引
                    IndexInfo index = indexMap.computeIfAbsent(indexName, k -> 
                        IndexInfo.builder()
                            .tableId(null) // 需要在保存时设置
                            .name(indexName)
                            .type("BTREE") // DB2默认使用BTREE
                            .isUnique(!nonUnique)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .columns(new ArrayList<>())
                            .build()
                    );
                    
                    // 创建索引列
                    IndexColumnInfo indexColumn = IndexColumnInfo.builder()
                            .indexId(null) // 需要在保存时设置
                            .columnId(null) // 需要在保存时设置
                            .ordinalPosition(ordinalPosition)
                            .sortOrder(ascOrDesc)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    
                    index.getColumns().add(indexColumn);
                }
            }
        } catch (SQLException e) {
            log.error("Failed to get indexes from DB2 database: {}", dataSource.getName(), e);
            throw DataSourceException.metadataExtractionError("Failed to get indexes: " + e.getMessage(), e);
        }
        
        return new ArrayList<>(indexMap.values());
    }
    
    @Override
    public List<ForeignKeyInfo> getForeignKeys(DataSource dataSource, String schemaName, String tableName) {
        Map<String, ForeignKeyInfo> foreignKeyMap = new HashMap<>();
        
        try (Connection connection = getConnection(dataSource)) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            try (ResultSet rs = metaData.getImportedKeys(null, schemaName, tableName)) {
                while (rs.next()) {
                    String fkName = rs.getString("FK_NAME");
                    String pkTableName = rs.getString("PKTABLE_NAME");
                    String pkColumnName = rs.getString("PKCOLUMN_NAME");
                    String fkColumnName = rs.getString("FKCOLUMN_NAME");
                    int keySeq = rs.getInt("KEY_SEQ");
                    int updateRule = rs.getInt("UPDATE_RULE");
                    int deleteRule = rs.getInt("DELETE_RULE");
                    
                    // 获取或创建外键
                    ForeignKeyInfo foreignKey = foreignKeyMap.computeIfAbsent(fkName, k -> 
                        ForeignKeyInfo.builder()
                            .name(fkName)
                            .sourceTableId(null) // 需要在保存时设置
                            .targetTableId(null) // 需要在保存时设置
                            .updateRule(getForeignKeyRuleName(updateRule))
                            .deleteRule(getForeignKeyRuleName(deleteRule))
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .columns(new ArrayList<>())
                            .build()
                    );
                    
                    // 创建外键列映射
                    ForeignKeyColumnInfo foreignKeyColumn = ForeignKeyColumnInfo.builder()
                            .foreignKeyId(null) // 需要在保存时设置
                            .sourceColumnId(null) // 需要在保存时设置
                            .targetColumnId(null) // 需要在保存时设置
                            .ordinalPosition(keySeq)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    
                    foreignKey.getColumns().add(foreignKeyColumn);
                }
            }
        } catch (SQLException e) {
            log.error("Failed to get foreign keys from DB2 database: {}", dataSource.getName(), e);
            throw DataSourceException.metadataExtractionError("Failed to get foreign keys: " + e.getMessage(), e);
        }
        
        return new ArrayList<>(foreignKeyMap.values());
    }
    
    @Override
    public List<List<Object>> executeQuery(DataSource dataSource, String sql, Object... params) {
        List<List<Object>> results = new ArrayList<>();
        
        try (Connection connection = getConnection(dataSource);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            // 设置参数
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            
            // 执行查询
            try (ResultSet rs = stmt.executeQuery()) {
                int columnCount = rs.getMetaData().getColumnCount();
                
                // 添加列名作为第一行
                List<Object> columnNames = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    columnNames.add(rs.getMetaData().getColumnName(i));
                }
                results.add(columnNames);
                
                // 添加数据行
                while (rs.next()) {
                    List<Object> row = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(rs.getObject(i));
                    }
                    results.add(row);
                }
            }
        } catch (SQLException e) {
            log.error("Failed to execute query on DB2 database: {}", dataSource.getName(), e);
            throw DataSourceException.metadataExtractionError("Failed to execute query: " + e.getMessage(), e);
        }
        
        return results;
    }
    
    @Override
    public long getEstimatedRowCount(DataSource dataSource, String schemaName, String tableName) {
        String sql = "SELECT CARD FROM SYSIBM.SYSTABLES WHERE CREATOR = ? AND NAME = ?";
        
        try (Connection connection = getConnection(dataSource);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, schemaName);
            stmt.setString(2, tableName);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            log.warn("Failed to get estimated row count for table {}.{}: {}", schemaName, tableName, e.getMessage());
        }
        
        return 0;
    }
    
    @Override
    public long getDataSize(DataSource dataSource, String schemaName, String tableName) {
        String sql = "SELECT FPAGES * 4096 FROM SYSIBM.SYSTABLES WHERE CREATOR = ? AND NAME = ?";
        
        try (Connection connection = getConnection(dataSource);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, schemaName);
            stmt.setString(2, tableName);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            log.warn("Failed to get data size for table {}.{}: {}", schemaName, tableName, e.getMessage());
        }
        
        return 0;
    }
    
    @Override
    public long getIndexSize(DataSource dataSource, String schemaName, String tableName) {
        String sql = "SELECT SUM(NLEAF * 4096) FROM SYSIBM.SYSINDEXES WHERE TBCREATOR = ? AND TBNAME = ?";
        
        try (Connection connection = getConnection(dataSource);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, schemaName);
            stmt.setString(2, tableName);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            log.warn("Failed to get index size for table {}.{}: {}", schemaName, tableName, e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * 判断是否为系统模式
     */
    private boolean isSystemSchema(String schemaName) {
        return "SYSIBM".equalsIgnoreCase(schemaName) ||
               "SYSCAT".equalsIgnoreCase(schemaName) ||
               "SYSSTAT".equalsIgnoreCase(schemaName) ||
               "SYSFUN".equalsIgnoreCase(schemaName) ||
               "SYSPROC".equalsIgnoreCase(schemaName) ||
               "SYSIBMADM".equalsIgnoreCase(schemaName);
    }
    
    /**
     * 判断是否为主键索引
     */
    private boolean isPrimaryKeyIndex(String indexName) {
        return indexName != null && indexName.toUpperCase().startsWith("SQL");
    }
    
    /**
     * 判断列是否为自增列
     */
    private boolean isAutoIncrementColumn(Connection connection, String schemaName, String tableName, String columnName) {
        String sql = "SELECT IDENTITY FROM SYSCAT.COLUMNS WHERE TABSCHEMA = ? AND TABNAME = ? AND COLNAME = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, schemaName);
            stmt.setString(2, tableName);
            stmt.setString(3, columnName);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String identity = rs.getString(1);
                    return "Y".equals(identity);
                }
            }
        } catch (SQLException e) {
            log.warn("Failed to check if column is auto increment: {}.{}.{}: {}", 
                    schemaName, tableName, columnName, e.getMessage());
        }
        
        return false;
    }
    
    /**
     * 获取外键规则名称
     */
    private String getForeignKeyRuleName(int rule) {
        switch (rule) {
            case DatabaseMetaData.importedKeyCascade:
                return "CASCADE";
            case DatabaseMetaData.importedKeySetNull:
                return "SET NULL";
            case DatabaseMetaData.importedKeySetDefault:
                return "SET DEFAULT";
            case DatabaseMetaData.importedKeyRestrict:
                return "RESTRICT";
            case DatabaseMetaData.importedKeyNoAction:
                return "NO ACTION";
            default:
                return "UNKNOWN";
        }
    }
}