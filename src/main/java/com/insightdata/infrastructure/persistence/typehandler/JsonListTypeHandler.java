package com.insightdata.infrastructure.persistence.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON List类型处理器
 * 用于处理数据库中JSON类型字段与Java List类型的转换
 */
@MappedTypes(List.class)
public class JsonListTypeHandler extends BaseTypeHandler<List<Long>> {
    
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<List<Long>> TYPE_REFERENCE = new TypeReference<List<Long>>() {};
    
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Long> parameter, JdbcType jdbcType)
            throws SQLException {
        try {
            ps.setString(i, OBJECT_MAPPER.writeValueAsString(parameter));
        } catch (Exception e) {
            throw new SQLException("Error converting List to JSON string", e);
        }
    }
    
    @Override
    public List<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return parseJson(json);
    }
    
    @Override
    public List<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return parseJson(json);
    }
    
    @Override
    public List<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return parseJson(json);
    }
    
    private List<Long> parseJson(String json) throws SQLException {
        if (json == null) {
            return new ArrayList<>();
        }
        try {
            return OBJECT_MAPPER.readValue(json, TYPE_REFERENCE);
        } catch (Exception e) {
            throw new SQLException("Error parsing JSON string to List", e);
        }
    }
}