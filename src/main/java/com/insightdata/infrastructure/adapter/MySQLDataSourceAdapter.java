package com.insightdata.infrastructure.adapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.insightdata.common.exception.DataSourceException;
import com.insightdata.domain.model.DataSource;
import com.insightdata.domain.model.metadata.SchemaInfo;

@Component
public class MySQLDataSourceAdapter implements DataSourceAdapter {

    @Override
    public boolean testConnection(DataSource dataSource) throws DataSourceException {
        try (Connection connection = getConnection(dataSource)) {
            return connection.isValid(5);
        } catch (SQLException e) {
            throw new DataSourceException("Failed to test connection to MySQL database: " + dataSource.getName(), e);
        }
    }

    @Override
    public List<SchemaInfo> getSchemas(DataSource dataSource) throws DataSourceException {
        try (Connection connection = getConnection(dataSource)) {
            List<SchemaInfo> schemas = new ArrayList<>();
            ResultSet rs = connection.getMetaData().getSchemas();
            while (rs.next()) {
                schemas.add(SchemaInfo.builder()
                        .name(rs.getString("TABLE_SCHEM"))
                        .dataSourceId(dataSource.getId())
                        .build());
            }
            return schemas;
        } catch (SQLException e) {
            throw new DataSourceException("Failed to get schemas from MySQL database: " + dataSource.getName(), e);
        }
    }

    @Override
    public SchemaInfo getSchema(DataSource dataSource, String schemaName) throws DataSourceException {
        try (Connection connection = getConnection(dataSource)) {
            return SchemaInfo.builder()
                    .name(schemaName)
                    .dataSourceId(dataSource.getId())
                    .build();
        } catch (SQLException e) {
            throw new DataSourceException("Failed to get schema from MySQL database: " + dataSource.getName(), e);
        }
    }

    private Connection getConnection(DataSource dataSource) throws SQLException {
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
    }
}
