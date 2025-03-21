package com.insightdata.domain.querybuilder.util;

import com.insightdata.domain.querybuilder.api.QueryModelContract;
import com.insightdata.domain.querybuilder.model.QueryModel;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * QueryModelMapper 的单元测试类
 */
class QueryModelMapperTest {

    @Test
    void shouldReturnNullWhenInputIsNull() {
        assertNull(QueryModelMapper.toDomain(null));
    }

    @Test
    void shouldCopyAllPropertiesCorrectly() {
        // 准备测试数据
        QueryModel source = new QueryModel();
        source.setId("test-id");
        source.setName("test-name");
        source.setTables(Arrays.asList("table1", "table2"));
        source.setFields(Arrays.asList("field1", "field2"));
        source.setJoins(Arrays.asList("join1", "join2"));
        source.setFilter("test-filter");
        source.setGroupBy(Arrays.asList("group1", "group2"));
        source.setOrderBy(Arrays.asList("order1", "order2"));
        
        Map<String, Object> params = new HashMap<>();
        params.put("key1", "value1");
        params.put("key2", 123);
        source.setParameters(params);

        // 执行转换
        QueryModel target = QueryModelMapper.toDomain(source);

        // 验证结果
        assertNotNull(target);
        assertEquals(source.getId(), target.getId());
        assertEquals(source.getName(), target.getName());
        assertEquals(source.getFilter(), target.getFilter());
        
        // 验证集合是新的实例
        assertNotSame(source.getTables(), target.getTables());
        assertNotSame(source.getFields(), target.getFields());
        assertNotSame(source.getJoins(), target.getJoins());
        assertNotSame(source.getGroupBy(), target.getGroupBy());
        assertNotSame(source.getOrderBy(), target.getOrderBy());
        assertNotSame(source.getParameters(), target.getParameters());
        
        // 验证集合内容相同
        assertEquals(source.getTables(), target.getTables());
        assertEquals(source.getFields(), target.getFields());
        assertEquals(source.getJoins(), target.getJoins());
        assertEquals(source.getGroupBy(), target.getGroupBy());
        assertEquals(source.getOrderBy(), target.getOrderBy());
        assertEquals(source.getParameters(), target.getParameters());
    }

    @Test
    void shouldHandleEmptyCollections() {
        // 准备测试数据
        QueryModel source = new QueryModel();
        source.setId("test-id");
        source.setName("test-name");

        // 执行转换
        QueryModel target = QueryModelMapper.toDomain(source);

        // 验证结果
        assertNotNull(target);
        assertNotNull(target.getTables());
        assertNotNull(target.getFields());
        assertNotNull(target.getJoins());
        assertNotNull(target.getGroupBy());
        assertNotNull(target.getOrderBy());
        assertNotNull(target.getParameters());
        
        assertTrue(target.getTables().isEmpty());
        assertTrue(target.getFields().isEmpty());
        assertTrue(target.getJoins().isEmpty());
        assertTrue(target.getGroupBy().isEmpty());
        assertTrue(target.getOrderBy().isEmpty());
        assertTrue(target.getParameters().isEmpty());
    }

    @Test
    void shouldHandleCollectionModification() {
        // 准备测试数据
        QueryModel source = new QueryModel();
        List<String> tables = Arrays.asList("table1", "table2");
        source.setTables(tables);

        // 执行转换
        QueryModel target = QueryModelMapper.toDomain(source);

        // 修改源对象的集合
        try {
            source.getTables().add("table3");
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // 预期的异常
        }

        // 验证目标对象的集合没有被修改
        assertEquals(2, target.getTables().size());
        assertTrue(target.getTables().contains("table1"));
        assertTrue(target.getTables().contains("table2"));
    }
}