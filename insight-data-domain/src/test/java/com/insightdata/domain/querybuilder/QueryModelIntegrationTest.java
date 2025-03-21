package com.insightdata.domain.querybuilder;

import com.insightdata.domain.querybuilder.api.QueryModelContract;
import com.insightdata.domain.querybuilder.model.QueryModel;
import com.insightdata.domain.querybuilder.util.QueryModelMapper;
import com.insightdata.facade.querybuilder.QueryModelDto;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 查询模型架构集成测试
 * 验证 Domain 模型、DTO 和转换器之间的交互
 */
class QueryModelIntegrationTest {

    @Test
    void shouldConvertBetweenDomainAndDto() {
        // 创建领域模型
        QueryModel domainModel = new QueryModel();
        domainModel.setId("test-id");
        domainModel.setName("test-name");
        domainModel.addTable("table1");
        domainModel.addField("field1");
        domainModel.addJoin("join1");
        domainModel.setFilter("filter1");
        domainModel.addGroupBy("group1");
        domainModel.addOrderBy("order1");
        domainModel.addParameter("key1", "value1");

        // 转换为 DTO
        QueryModelDto dto = new QueryModelDto(domainModel);

        // 验证 DTO 数据
        assertEquals(domainModel.getId(), dto.getId());
        assertEquals(domainModel.getName(), dto.getName());
        assertEquals(domainModel.getTables(), dto.getTables());
        assertEquals(domainModel.getFields(), dto.getFields());
        assertEquals(domainModel.getJoins(), dto.getJoins());
        assertEquals(domainModel.getFilter(), dto.getFilter());
        assertEquals(domainModel.getGroupBy(), dto.getGroupBy());
        assertEquals(domainModel.getOrderBy(), dto.getOrderBy());
        assertEquals(domainModel.getParameters(), dto.getParameters());

        // 转换回领域模型
        QueryModel convertedModel = QueryModelMapper.toDomain(dto);

        // 验证转换后的领域模型
        assertEquals(domainModel.getId(), convertedModel.getId());
        assertEquals(domainModel.getName(), convertedModel.getName());
        assertEquals(domainModel.getTables(), convertedModel.getTables());
        assertEquals(domainModel.getFields(), convertedModel.getFields());
        assertEquals(domainModel.getJoins(), convertedModel.getJoins());
        assertEquals(domainModel.getFilter(), convertedModel.getFilter());
        assertEquals(domainModel.getGroupBy(), convertedModel.getGroupBy());
        assertEquals(domainModel.getOrderBy(), convertedModel.getOrderBy());
        assertEquals(domainModel.getParameters(), convertedModel.getParameters());
    }

    @Test
    void shouldMaintainContractBehavior() {
        // 创建不同的实现
        List<QueryModelContract> implementations = Arrays.asList(
            new QueryModel(),
            new QueryModelDto()
        );

        // 测试所有实现的行为一致性
        for (QueryModelContract impl : implementations) {
            // 测试空集合初始化
            assertNotNull(impl.getTables());
            assertNotNull(impl.getFields());
            assertNotNull(impl.getJoins());
            assertNotNull(impl.getGroupBy());
            assertNotNull(impl.getOrderBy());
            assertNotNull(impl.getParameters());

            // 测试设置和获取属性
            impl.setId("test-id");
            impl.setName("test-name");
            impl.setTables(Arrays.asList("table1", "table2"));
            impl.setFields(Arrays.asList("field1", "field2"));
            impl.setJoins(Arrays.asList("join1", "join2"));
            impl.setFilter("test-filter");
            impl.setGroupBy(Arrays.asList("group1", "group2"));
            impl.setOrderBy(Arrays.asList("order1", "order2"));
            
            Map<String, Object> params = new HashMap<>();
            params.put("key1", "value1");
            impl.setParameters(params);

            // 验证属性值
            assertEquals("test-id", impl.getId());
            assertEquals("test-name", impl.getName());
            assertEquals(2, impl.getTables().size());
            assertEquals(2, impl.getFields().size());
            assertEquals(2, impl.getJoins().size());
            assertEquals("test-filter", impl.getFilter());
            assertEquals(2, impl.getGroupBy().size());
            assertEquals(2, impl.getOrderBy().size());
            assertEquals(1, impl.getParameters().size());
        }
    }

    @Test
    void shouldHandleNullValues() {
        // 测试所有实现对 null 值的处理
        List<QueryModelContract> implementations = Arrays.asList(
            new QueryModel(),
            new QueryModelDto()
        );

        for (QueryModelContract impl : implementations) {
            impl.setTables(null);
            impl.setFields(null);
            impl.setJoins(null);
            impl.setGroupBy(null);
            impl.setOrderBy(null);
            impl.setParameters(null);

            assertNotNull(impl.getTables());
            assertNotNull(impl.getFields());
            assertNotNull(impl.getJoins());
            assertNotNull(impl.getGroupBy());
            assertNotNull(impl.getOrderBy());
            assertNotNull(impl.getParameters());
        }
    }

    @Test
    void shouldPreserveCollectionIndependence() {
        // 测试所有实现的集合独立性
        List<QueryModelContract> implementations = Arrays.asList(
            new QueryModel(),
            new QueryModelDto()
        );

        for (QueryModelContract impl : implementations) {
            // 设置初始集合
            List<String> tables = new ArrayList<>(Arrays.asList("table1", "table2"));
            impl.setTables(tables);

            // 修改原始集合
            tables.add("table3");

            // 验证实现中的集合没有被修改
            assertEquals(2, impl.getTables().size());
            assertFalse(impl.getTables().contains("table3"));
        }
    }
}