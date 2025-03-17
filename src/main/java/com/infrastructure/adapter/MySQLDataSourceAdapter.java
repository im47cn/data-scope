package com.infrastructure.adapter;

import com.common.exception.DataSourceException;
import com.domain.model.DataSource;
import com.domain.model.metadata.*;
import com.nlquery.executor.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

@Component
public class MySQLDataSourceAdapter implements DataSourceAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLDataSourceAdapter.class);

    @Override
    public boolean testConnection(DataSource dataSource) {
        try (Connection connection = getConnection(dataSource)) {
            return connection.isValid(5);
        } catch (SQLException e) {
            LOGGER.error("Failed to test connection to MySQL database: {}", dataSource.getName(), e);
            return false;
        }
    }

    @Override
    public Connection getConnection(DataSource dataSource) throws SQLException {
        try {
            Properties props = new Properties();
            props.setProperty("user", dataSource.getUsername());
            props.setProperty("password", dataSource.getEncryptedPassword());

            if (dataSource.getConnectionProperties() != null) {
                dataSource.getConnectionProperties().forEach(props::setProperty);
            }

            return DriverManager.getConnection(
                    "jdbc:mysql://" + dataSource.getHost() + ":" + dataSource.getPort() + "/" + dataSource.getDatabaseName(),
                    props
            );
        } catch (SQLException e) {
            LOGGER.error("Failed to connect to MySQL database: {}", dataSource.getName(), e);
            throw DataSourceException.connectionError("Failed to connect to MySQL database: " + e.getMessage(), e);
        }
    }

    @Override
    public List<SchemaInfo> getSchemas(DataSource dataSource) {
        try (Connection connection = getConnection(dataSource)) {
            List<SchemaInfo> schemas = new ArrayList<>();
            try (ResultSet rs = connection.getMetaData().getSchemas()) {
                while (rs.next()) {
                    schemas.add(SchemaInfo.builder()
                            .name(rs.getString("TABLE_SCHEM"))
                            .dataSourceId(dataSource.getId())
                            .build());
                }
            }
            return schemas;
        } catch (SQLException e) {
            LOGGER.error("Failed to get schemas from MySQL database: {}", dataSource.getName(), e);
            throw DataSourceException.metadataExtractionError("Failed to get schemas: " + e.getMessage(), e);
        }
    }

    @Override
    public SchemaInfo getSchema(DataSource dataSource, String schemaName) {
        try (Connection connection = getConnection(dataSource)) {
            try (ResultSet rs = connection.getMetaData().getSchemas()) {
                while (rs.next()) {
                    String currentSchema = rs.getString("TABLE_SCHEM");
                    if (currentSchema.equals(schemaName)) {
                        return SchemaInfo.builder()
                                .name(schemaName)
                                .dataSourceId(dataSource.getId())
                                .build();
                    }
                }
            }
            return null;
        } catch (SQLException e) {
            LOGGER.error("Failed to get schema from MySQL database: {}", dataSource.getName(), e);
            throw DataSourceException.metadataExtractionError("Failed to get schema: " + e.getMessage(), e);
        }
    }

    @Override
    public List<TableInfo> getTables(DataSource dataSource, String schemaName) {
        try (Connection connection = getConnection(dataSource)) {
            List<TableInfo> tables = new ArrayList<>();
            try (ResultSet rs = connection.getMetaData().getTables(schemaName, null, "%", new String[]{"TABLE", "VIEW"})) {
                while (rs.next()) {
                    String tableName = rs.getString("TABLE_NAME");
                    String tableType = rs.getString("TABLE_TYPE");
                    String remarks = rs.getString("REMARKS");

                    TableInfo table = TableInfo.builder()
                            .name(tableName)
                            .type(tableType)
                            .description(remarks)
                            .schemaName(schemaName)
                            .dataSourceId(dataSource.getId())
                            .build();

                    tables.add(table);
                }
            }

            // 获取表的行数和大小信息
            for (TableInfo table : tables) {
                table.setRowCount(getRowCount(dataSource, schemaName, table.getName()));
//                table.setDataSize(getDataSize(dataSource, schemaName, table.getName()));
//                table.setIndexSize(getIndexSize(dataSource, schemaName, table.getName()));
            }

            return tables;
        } catch (SQLException e) {
            LOGGER.error("Failed to get tables from MySQL database: {}", dataSource.getName(), e);
            throw DataSourceException.metadataExtractionError("Failed to get tables: " + e.getMessage(), e);
        }
    }

    @Override
    public TableInfo getTable(DataSource dataSource, String schemaName, String tableName) {
        try (Connection connection = getConnection(dataSource)) {
            try (ResultSet rs = connection.getMetaData().getTables(schemaName, null, tableName, new String[]{"TABLE", "VIEW"})) {
                if (rs.next()) {
                    String tableType = rs.getString("TABLE_TYPE");
                    String remarks = rs.getString("REMARKS");

                    TableInfo table = TableInfo.builder()
                            .name(tableName)
                            .type(tableType)
                            .description(remarks)
                            .schemaName(schemaName)
                            .dataSourceId(dataSource.getId())
                            .build();

                    table.setRowCount(getRowCount(dataSource, schemaName, tableName));
//                    table.setDataSize(getDataSize(dataSource, schemaName, tableName));
//                    table.setIndexSize(getIndexSize(dataSource, schemaName, tableName));

                    return table;
                }
            }
            return null;
        } catch (SQLException e) {
            LOGGER.error("Failed to get table from MySQL database: {}", dataSource.getName(), e);
            throw DataSourceException.metadataExtractionError("Failed to get table: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ColumnInfo> getColumns(DataSource dataSource, String schemaName, String tableName) {
        try (Connection connection = getConnection(dataSource)) {
            List<ColumnInfo> columns = new ArrayList<>();
            Map<String, Boolean> primaryKeys = new HashMap<>();

            // 获取主键信息
            try (ResultSet pkRs = connection.getMetaData().getPrimaryKeys(schemaName, null, tableName)) {
                while (pkRs.next()) {
                    primaryKeys.put(pkRs.getString("COLUMN_NAME"), true);
                }
            }

            // 获取列信息
            try (ResultSet rs = connection.getMetaData().getColumns(schemaName, null, tableName, "%")) {
                while (rs.next()) {
                    String columnName = rs.getString("COLUMN_NAME");
                    String dataType = rs.getString("TYPE_NAME");
                    int ordinalPosition = rs.getInt("ORDINAL_POSITION");
                    int columnSize = rs.getInt("COLUMN_SIZE");
                    int decimalDigits = rs.getInt("DECIMAL_DIGITS");
                    boolean nullable = rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable;
                    String defaultValue = rs.getString("COLUMN_DEF");
                    String remarks = rs.getString("REMARKS");
                    boolean isAutoIncrement = "YES".equals(rs.getString("IS_AUTOINCREMENT"));

                    ColumnInfo column = ColumnInfo.builder()
                            .name(columnName)
                            .dataType(dataType)
                            .ordinalPosition(ordinalPosition)
                            .length(columnSize)
                            .numericPrecision(columnSize)
                            .numericScale(decimalDigits)
                            .nullable(nullable)
                            .defaultValue(defaultValue)
                            .description(remarks)
                            .primaryKey(primaryKeys.containsKey(columnName))
                            .autoIncrement(isAutoIncrement)
                            .build();

                    columns.add(column);
                }
            }

            return columns;
        } catch (SQLException e) {
            LOGGER.error("Failed to get columns from MySQL database: {}", dataSource.getName(), e);
            throw DataSourceException.metadataExtractionError("Failed to get columns: " + e.getMessage(), e);
        }
    }

    @Override
    public List<IndexInfo> getIndexes(DataSource dataSource, String schemaName, String tableName) {
        try (Connection connection = getConnection(dataSource)) {
            Map<String, IndexInfo> indexMap = new HashMap<>();

            try (ResultSet rs = connection.getMetaData().getIndexInfo(schemaName, null, tableName, false, true)) {
                while (rs.next()) {
                    String indexName = rs.getString("INDEX_NAME");
                    if (indexName == null) {
                        continue;
                    }

                    boolean nonUnique = rs.getBoolean("NON_UNIQUE");
                    String columnName = rs.getString("COLUMN_NAME");
                    int ordinalPosition = rs.getInt("ORDINAL_POSITION");
                    String ascOrDesc = rs.getString("ASC_OR_DESC");

                    IndexInfo index = indexMap.computeIfAbsent(indexName, k ->
                            IndexInfo.builder()
                                    .name(indexName)
                                    .type("BTREE")
                                    .unique(!nonUnique)
                                    .columns(new ArrayList<>())
                                    .build()
                    );

                    IndexColumnInfo indexColumn = IndexColumnInfo.builder()
                            .columnName(columnName)
                            .ordinalPosition(ordinalPosition)
                            .sortOrder(ascOrDesc)
                            .build();

                    index.getColumns().add(indexColumn);
                }
            }

            return new ArrayList<>(indexMap.values());
        } catch (SQLException e) {
            LOGGER.error("Failed to get indexes from MySQL database: {}", dataSource.getName(), e);
            throw DataSourceException.metadataExtractionError("Failed to get indexes: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ForeignKeyInfo> getForeignKeys(DataSource dataSource, String schemaName, String tableName) {
        try (Connection connection = getConnection(dataSource)) {
            Map<String, ForeignKeyInfo> foreignKeyMap = new HashMap<>();

            try (ResultSet rs = connection.getMetaData().getImportedKeys(schemaName, null, tableName)) {
                while (rs.next()) {
                    String fkName = rs.getString("FK_NAME");
                    String pkTableName = rs.getString("PKTABLE_NAME");
                    String pkColumnName = rs.getString("PKCOLUMN_NAME");
                    String fkColumnName = rs.getString("FKCOLUMN_NAME");
                    int keySeq = rs.getInt("KEY_SEQ");
                    int updateRule = rs.getInt("UPDATE_RULE");
                    int deleteRule = rs.getInt("DELETE_RULE");

                    ForeignKeyInfo foreignKey = foreignKeyMap.computeIfAbsent(fkName, k ->
                            ForeignKeyInfo.builder()
                                    .name(fkName)
                                    .sourceTableName(tableName)
                                    .targetTableName(pkTableName)
                                    .updateRule(getForeignKeyRuleName(updateRule))
                                    .deleteRule(getForeignKeyRuleName(deleteRule))
                                    .columns(new ArrayList<>())
                                    .build()
                    );

                    ForeignKeyColumnInfo foreignKeyColumn = ForeignKeyColumnInfo.builder()
                            .sourceTableName(tableName)
                            .sourceColumnName(fkColumnName)
                            .targetTableName(pkTableName)
                            .targetColumnName(pkColumnName)
                            .ordinalPosition(keySeq)
                            .build();

                    foreignKey.getColumns().add(foreignKeyColumn);
                }
            }

            return new ArrayList<>(foreignKeyMap.values());
        } catch (SQLException e) {
            LOGGER.error("Failed to get foreign keys from MySQL database: {}", dataSource.getName(), e);
            throw DataSourceException.metadataExtractionError("Failed to get foreign keys: " + e.getMessage(), e);
        }
    }

    @Override
    public long getDataSize(DataSource dataSource, String schemaName, String tableName) {
        String sql = "SELECT data_length + index_length FROM information_schema.tables " +
                "WHERE table_schema = ? AND table_name = ?";

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
            LOGGER.warn("Failed to get data size for table {}.{}: {}", schemaName, tableName, e.getMessage());
        }

        return 0;
    }

    @Override
    public long getRowCount(DataSource dataSource, String schemaName, String tableName) {
        String sql = "SELECT COUNT(*) FROM " + schemaName + "." + tableName;

        try (Connection connection = getConnection(dataSource);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.warn("Failed to get row count for table {}.{}: {}", schemaName, tableName, e.getMessage());
        }

        return 0;
    }

    @Override
    public long getIndexSize(DataSource dataSource, String schemaName, String tableName) {
        String sql = "SELECT SUM(index_length) FROM information_schema.tables " +
                "WHERE table_schema = ? AND table_name = ?";

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
            LOGGER.warn("Failed to get index size for table {}.{}: {}", schemaName, tableName, e.getMessage());
        }

        return 0;
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
            LOGGER.error("Failed to execute query on DB2 database: {}", dataSource.getName(), e);
            throw new RuntimeException("Failed to execute query on DB2 database", e);
        }
    }

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

    @Override
    public boolean isAutoIncrement(DataSource dataSource, String schemaName, String tableName, String columnName) {
        // TODO
        return false;
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
        return Arrays.asList("information_schema", "performance_schema", "sys");
    }

    @Override
    public boolean isSystemSchema(DataSource dataSource, String schemaName) {
        return getSystemSchemas(dataSource).contains(schemaName.toUpperCase());
    }

    @Override
    public boolean isSystemTable(DataSource dataSource, String schemaName, String tableName) {
        return isSystemSchema(dataSource, schemaName) || schemaName.startsWith("SYS");
    }
}
