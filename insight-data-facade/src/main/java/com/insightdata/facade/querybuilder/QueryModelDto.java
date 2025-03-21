package com.insightdata.facade.querybuilder;

import com.insightdata.domain.querybuilder.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object (DTO) for {@link QueryModel}.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryModelDto {
    private String id;
    private String name;
    private String description;
    private List<DataSourceReference> dataSources;
    private List<TableReference> tables;
    private List<FieldSelection> fields;
    private List<JoinDefinition> joins;
    private FilterGroup rootFilter;
    private List<GroupingExpression> groupBy;
    private List<SortExpression> orderBy;
    private Map<String, ParameterDefinition> parameters;
    private Map<String, Object> options;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private boolean isPublic;
    private List<String> tags;
    private ModelStatus status;
}