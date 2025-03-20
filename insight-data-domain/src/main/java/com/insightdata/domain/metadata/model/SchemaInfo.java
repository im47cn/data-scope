package com.insightdata.domain.metadata.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchemaInfo {
    private String name;
    private String dataSourceId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TableInfo> tables;
}