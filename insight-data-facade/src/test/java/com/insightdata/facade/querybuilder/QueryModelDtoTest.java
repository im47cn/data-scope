package com.insightdata.facade.querybuilder;

import com.insightdata.domain.querybuilder.api.QueryModelContract;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * QueryModelDto 的单元测试类
 */
class QueryModelDtoTest {

    @Test
    void shouldCreateEmptyDto() {
        QueryModelDto dto = new QueryModelDto();
        
        assertNotNull(dto.getTables());
        assertNotNull(dto.getFields());
        assertNotNull(dto.getJoins());
        assertNotNull(dto.getGroupBy());
        assertNotNull(dto.getOrderBy());
        assertNotNull(dto.getParameters());
        
        assertTrue(dto.getTables().isEmpty());
        assertTrue(dto.getFields().isEmpty());
        assertTrue(dto.getJoins().isEmpty());
        assertTrue(dto.getGroupBy().isEmpty());
        assertTrue(dto.getOrderBy().isEmpty());
        assertTrue(dto.getParameters().isEmpty());
    }

    @Test
    void shouldCreateFromContract() {
        // 准备测试数据
        QueryModelContract mockContract = Mockito.mock(QueryModelContract.class);
        when(mockContract.getId()).thenReturn("test-id");
        when(mockContract.getName()).thenReturn("test-name");
        when(mockContract.getTables()).thenReturn(Arrays.asList("table1", "table2"));
        when(mockContract.getFields()).thenReturn(Arrays.asList("field1", "field2"));
        when(mockContract.getJoins()).thenReturn(Arrays.asList("join1", "join2"));
        when(mockContract.getFilter()).thenReturn("test-filter");
        when(mockContract.getGroupBy()).thenReturn(Arrays.asList("group1", "group2"));
        when(mockContract.getOrderBy()).thenReturn(Arrays.asList("order1", "order2"));
        
        Map<String, Object> params = new HashMap<>();
        params.put("key1", "value1");
        params.put("key2", 123);
        when(mockContract.getParameters()).thenReturn(params);

        // 创建DTO
        QueryModelDto dto = new QueryModelDto(mockContract);

        // 验证数据正确复制
        assertEquals("test-id", dto.getId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-filter", dto.getFilter());
        
        assertEquals(2, dto.getTables().size());
        assertTrue(dto.getTables().contains("table1"));
        assertTrue(dto.getTables().contains("table2"));
        
        assertEquals(2, dto.getFields().size());
        assertTrue(dto.getFields().contains("field1"));
        assertTrue(dto.getFields().contains("field2"));
        
        assertEquals(2, dto.getJoins().size());
        assertTrue(dto.getJoins().contains("join1"));
        assertTrue(dto.getJoins().contains("join2"));
        
        assertEquals(2, dto.getGroupBy().size());
        assertTrue(dto.getGroupBy().contains("group1"));
        assertTrue(dto.getGroupBy().contains("group2"));
        
        assertEquals(2, dto.getOrderBy().size());
        assertTrue(dto.getOrderBy().contains("order1"));
        assertTrue(dto.getOrderBy().contains("order2"));
        
        assertEquals(2, dto.getParameters().size());
        assertEquals("value1", dto.getParameters().get("key1"));
        assertEquals(123, dto.getParameters().get("key2"));
    }

    @Test
    void shouldCreateUsingBuilder() {
        QueryModelDto dto = QueryModelDto.builder()
                .id("test-id")
                .name("test-name")
                .tables(Arrays.asList("table1", "table2"))
                .fields(Arrays.asList("field1", "field2"))
                .joins(Arrays.asList("join1", "join2"))
                .filter("test-filter")
                .groupBy(Arrays.asList("group1", "group2"))
                .orderBy(Arrays.asList("order1", "order2"))
                .parameters(Collections.singletonMap("key1", "value1"))
                .build();

        assertEquals("test-id", dto.getId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-filter", dto.getFilter());
        assertEquals(2, dto.getTables().size());
        assertEquals(2, dto.getFields().size());
        assertEquals(2, dto.getJoins().size());
        assertEquals(2, dto.getGroupBy().size());
        assertEquals(2, dto.getOrderBy().size());
        assertEquals(1, dto.getParameters().size());
        assertEquals("value1", dto.getParameters().get("key1"));
    }

    @Test
    void shouldHandleNullContract() {
        QueryModelDto dto = new QueryModelDto(null);
        
        assertNotNull(dto.getTables());
        assertNotNull(dto.getFields());
        assertNotNull(dto.getJoins());
        assertNotNull(dto.getGroupBy());
        assertNotNull(dto.getOrderBy());
        assertNotNull(dto.getParameters());
    }

    @Test
    void shouldCreateDefensiveCopies() {
        // 准备测试数据
        List<String> tables = new ArrayList<>(Arrays.asList("table1", "table2"));
        Map<String, Object> params = new HashMap<>();
        params.put("key1", "value1");

        // 使用构建器创建DTO
        QueryModelDto dto = QueryModelDto.builder()
                .tables(tables)
                .parameters(params)
                .build();

        // 修改原始数据
        tables.add("table3");
        params.put("key2", "value2");

        // 验证DTO中的数据没有被修改
        assertEquals(2, dto.getTables().size());
        assertEquals(1, dto.getParameters().size());
        assertFalse(dto.getTables().contains("table3"));
        assertNull(dto.getParameters().get("key2"));
    }
}