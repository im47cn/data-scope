package com.insightdata.domain.querybuilder.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import com.insightdata.domain.querybuilder.api.QueryModelContract;

/**
 * QueryModel 的单元测试类
 */
class QueryModelTest {

    @Test
    void shouldCreateEmptyModel() {
        QueryModel model = new QueryModel();
        
        assertNotNull(model.getTables());
        assertNotNull(model.getFields());
        assertNotNull(model.getJoins());
        assertNotNull(model.getGroupBy());
        assertNotNull(model.getOrderBy());
        assertNotNull(model.getParameters());
        
        assertTrue(model.getTables().isEmpty());
        assertTrue(model.getFields().isEmpty());
        assertTrue(model.getJoins().isEmpty());
        assertTrue(model.getGroupBy().isEmpty());
        assertTrue(model.getOrderBy().isEmpty());
        assertTrue(model.getParameters().isEmpty());
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

        // 创建模型
        QueryModel model = new QueryModel(mockContract);

        // 验证数据正确复制
        assertEquals("test-id", model.getId());
        assertEquals("test-name", model.getName());
        assertEquals("test-filter", model.getFilter());
        
        assertEquals(2, model.getTables().size());
        assertTrue(model.getTables().contains("table1"));
        assertTrue(model.getTables().contains("table2"));
        
        assertEquals(2, model.getFields().size());
        assertTrue(model.getFields().contains("field1"));
        assertTrue(model.getFields().contains("field2"));
        
        assertEquals(2, model.getJoins().size());
        assertTrue(model.getJoins().contains("join1"));
        assertTrue(model.getJoins().contains("join2"));
        
        assertEquals(2, model.getGroupBy().size());
        assertTrue(model.getGroupBy().contains("group1"));
        assertTrue(model.getGroupBy().contains("group2"));
        
        assertEquals(2, model.getOrderBy().size());
        assertTrue(model.getOrderBy().contains("order1"));
        assertTrue(model.getOrderBy().contains("order2"));
        
        assertEquals(2, model.getParameters().size());
        assertEquals("value1", model.getParameters().get("key1"));
        assertEquals(123, model.getParameters().get("key2"));
    }

    @Test
    void shouldHandleNullContract() {
        QueryModel model = new QueryModel(null);
        
        assertNotNull(model.getTables());
        assertNotNull(model.getFields());
        assertNotNull(model.getJoins());
        assertNotNull(model.getGroupBy());
        assertNotNull(model.getOrderBy());
        assertNotNull(model.getParameters());
    }

    @Test
    void shouldAddItemsCorrectly() {
        QueryModel model = new QueryModel();
        
        model.addTable("table1");
        model.addField("field1");
        model.addJoin("join1");
        model.addGroupBy("group1");
        model.addOrderBy("order1");
        model.addParameter("key1", "value1");

        assertEquals(1, model.getTables().size());
        assertEquals(1, model.getFields().size());
        assertEquals(1, model.getJoins().size());
        assertEquals(1, model.getGroupBy().size());
        assertEquals(1, model.getOrderBy().size());
        assertEquals(1, model.getParameters().size());

        assertEquals("table1", model.getTables().get(0));
        assertEquals("field1", model.getFields().get(0));
        assertEquals("join1", model.getJoins().get(0));
        assertEquals("group1", model.getGroupBy().get(0));
        assertEquals("order1", model.getOrderBy().get(0));
        assertEquals("value1", model.getParameters().get("key1"));
    }

    @Test
    void shouldHandleInvalidInput() {
        QueryModel model = new QueryModel();
        
        model.addTable(null);
        model.addTable("");
        model.addTable("  ");
        
        model.addField(null);
        model.addField("");
        model.addField("  ");
        
        assertTrue(model.getTables().isEmpty());
        assertTrue(model.getFields().isEmpty());
    }

    @Test
    void shouldValidateCorrectly() {
        QueryModel model = new QueryModel();
        assertFalse(model.isValid());

        model.addTable("table1");
        assertFalse(model.isValid());

        model.addField("field1");
        assertTrue(model.isValid());
    }

    @Test
    void shouldClearAllCollections() {
        QueryModel model = new QueryModel();
        
        model.addTable("table1");
        model.addField("field1");
        model.addJoin("join1");
        model.addGroupBy("group1");
        model.addOrderBy("order1");
        model.addParameter("key1", "value1");

        assertFalse(model.getTables().isEmpty());
        assertFalse(model.getFields().isEmpty());
        assertFalse(model.getJoins().isEmpty());
        assertFalse(model.getGroupBy().isEmpty());
        assertFalse(model.getOrderBy().isEmpty());
        assertFalse(model.getParameters().isEmpty());

        model.clear();

        assertTrue(model.getTables().isEmpty());
        assertTrue(model.getFields().isEmpty());
        assertTrue(model.getJoins().isEmpty());
        assertTrue(model.getGroupBy().isEmpty());
        assertTrue(model.getOrderBy().isEmpty());
        assertTrue(model.getParameters().isEmpty());
    }
}