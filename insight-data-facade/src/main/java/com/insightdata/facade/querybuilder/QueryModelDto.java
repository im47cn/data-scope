package com.insightdata.facade.querybuilder;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询模型数据传输对象
 * 实现 QueryModelContract 接口，用于 Facade 层数据传输
 */
@Data
public class QueryModelDto implements QueryModelContract {
    /**
     * 查询模型ID
     */
    private String id;

    /**
     * 查询模型名称
     */
    private String name;

    /**
     * 查询涉及的表列表
     */
    private List<String> tables = new ArrayList<>();

    /**
     * 查询的字段列表
     */
    private List<String> fields = new ArrayList<>();

    /**
     * 表连接条件列表
     */
    private List<String> joins = new ArrayList<>();

    /**
     * 查询过滤条件
     */
    private String filter;

    /**
     * 分组字段列表
     */
    private List<String> groupBy = new ArrayList<>();

    /**
     * 排序字段列表
     */
    private List<String> orderBy = new ArrayList<>();

    /**
     * 查询参数映射
     */
    private Map<String, Object> parameters = new HashMap<>();

    /**
     * 创建一个空的查询模型DTO对象
     */
    public QueryModelDto() {
        // 使用字段初始化器创建空集合
    }

    /**
     * 使用现有的 QueryModelContract 创建DTO对象
     *
     * @param contract 查询模型契约对象
     */
    public QueryModelDto(QueryModelContract contract) {
        if (contract != null) {
            this.id = contract.getId();
            this.name = contract.getName();
            this.tables = new ArrayList<>(contract.getTables());
            this.fields = new ArrayList<>(contract.getFields());
            this.joins = new ArrayList<>(contract.getJoins());
            this.filter = contract.getFilter();
            this.groupBy = new ArrayList<>(contract.getGroupBy());
            this.orderBy = new ArrayList<>(contract.getOrderBy());
            this.parameters = new HashMap<>(contract.getParameters());
        }
    }

    /**
     * 创建一个查询模型DTO的构建器
     *
     * @return QueryModelDtoBuilder 实例
     */
    public static QueryModelDtoBuilder builder() {
        return new QueryModelDtoBuilder();
    }

    /**
     * 查询模型DTO的构建器类
     */
    public static class QueryModelDtoBuilder {
        private final QueryModelDto dto;

        private QueryModelDtoBuilder() {
            dto = new QueryModelDto();
        }

        public QueryModelDtoBuilder id(String id) {
            dto.setId(id);
            return this;
        }

        public QueryModelDtoBuilder name(String name) {
            dto.setName(name);
            return this;
        }

        public QueryModelDtoBuilder tables(List<String> tables) {
            dto.setTables(new ArrayList<>(tables));
            return this;
        }

        public QueryModelDtoBuilder fields(List<String> fields) {
            dto.setFields(new ArrayList<>(fields));
            return this;
        }

        public QueryModelDtoBuilder joins(List<String> joins) {
            dto.setJoins(new ArrayList<>(joins));
            return this;
        }

        public QueryModelDtoBuilder filter(String filter) {
            dto.setFilter(filter);
            return this;
        }

        public QueryModelDtoBuilder groupBy(List<String> groupBy) {
            dto.setGroupBy(new ArrayList<>(groupBy));
            return this;
        }

        public QueryModelDtoBuilder orderBy(List<String> orderBy) {
            dto.setOrderBy(new ArrayList<>(orderBy));
            return this;
        }

        public QueryModelDtoBuilder parameters(Map<String, Object> parameters) {
            dto.setParameters(new HashMap<>(parameters));
            return this;
        }

        public QueryModelDto build() {
            return dto;
        }
    }
}