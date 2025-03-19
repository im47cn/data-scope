package com.insightdata.domain.metadata.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ForeignKeyInfo类的单元测试
 * 测试列名列表与字符串格式转换方法
 */
public class ForeignKeyInfoTest {

    /**
     * 测试获取源列名字符串方法 - 多列情况
     */
    @Test
    @DisplayName("测试getSourceColumnNamesAsString方法 - 多列情况")
    public void testGetSourceColumnNamesAsString_MultipleColumns() {
        // 创建测试数据
        List<ForeignKeyColumnInfo> columns = Arrays.asList(
                ForeignKeyColumnInfo.builder().sourceColumnName("id").targetColumnName("user_id").build(),
                ForeignKeyColumnInfo.builder().sourceColumnName("dept_id").targetColumnName("department_id").build()
        );
        
        ForeignKeyInfo foreignKeyInfo = ForeignKeyInfo.builder()
                .name("fk_test")
                .sourceTableName("orders")
                .targetTableName("users")
                .columns(columns)
                .build();
        
        // 验证结果
        List<String> sourceColumnsList = foreignKeyInfo.getSourceColumnNames();
        String sourceColumnsString = foreignKeyInfo.getSourceColumnNamesAsString();
        
        assertEquals(2, sourceColumnsList.size());
        assertEquals("id,dept_id", sourceColumnsString);
        assertEquals(String.join(",", sourceColumnsList), sourceColumnsString);
    }
    
    /**
     * 测试获取目标列名字符串方法 - 多列情况
     */
    @Test
    @DisplayName("测试getTargetColumnNamesAsString方法 - 多列情况")
    public void testGetTargetColumnNamesAsString_MultipleColumns() {
        // 创建测试数据
        List<ForeignKeyColumnInfo> columns = Arrays.asList(
                ForeignKeyColumnInfo.builder().sourceColumnName("id").targetColumnName("user_id").build(),
                ForeignKeyColumnInfo.builder().sourceColumnName("dept_id").targetColumnName("department_id").build()
        );
        
        ForeignKeyInfo foreignKeyInfo = ForeignKeyInfo.builder()
                .name("fk_test")
                .sourceTableName("orders")
                .targetTableName("users")
                .columns(columns)
                .build();
        
        // 验证结果
        List<String> targetColumnsList = foreignKeyInfo.getTargetColumnNames();
        String targetColumnsString = foreignKeyInfo.getTargetColumnNamesAsString();
        
        assertEquals(2, targetColumnsList.size());
        assertEquals("user_id,department_id", targetColumnsString);
        assertEquals(String.join(",", targetColumnsList), targetColumnsString);
    }
    
    /**
     * 测试单列情况
     */
    @Test
    @DisplayName("测试列名方法 - 单列情况")
    public void testColumnNamesWithSingleColumn() {
        // 创建测试数据
        List<ForeignKeyColumnInfo> columns = Collections.singletonList(
                ForeignKeyColumnInfo.builder().sourceColumnName("id").targetColumnName("user_id").build()
        );
        
        ForeignKeyInfo foreignKeyInfo = ForeignKeyInfo.builder()
                .name("fk_test")
                .sourceTableName("orders")
                .targetTableName("users")
                .columns(columns)
                .build();
        
        // 验证结果
        assertEquals("id", foreignKeyInfo.getSourceColumnNamesAsString());
        assertEquals("user_id", foreignKeyInfo.getTargetColumnNamesAsString());
    }
    
    /**
     * 测试空列表情况
     */
    @Test
    @DisplayName("测试列名方法 - 空列表情况")
    public void testColumnNamesWithEmptyList() {
        // 创建测试数据
        List<ForeignKeyColumnInfo> columns = Collections.emptyList();
        
        ForeignKeyInfo foreignKeyInfo = ForeignKeyInfo.builder()
                .name("fk_test")
                .sourceTableName("orders")
                .targetTableName("users")
                .columns(columns)
                .build();
        
        // 验证结果
        assertTrue(foreignKeyInfo.getSourceColumnNames().isEmpty());
        assertTrue(foreignKeyInfo.getTargetColumnNames().isEmpty());
        assertEquals("", foreignKeyInfo.getSourceColumnNamesAsString());
        assertEquals("", foreignKeyInfo.getTargetColumnNamesAsString());
    }
}