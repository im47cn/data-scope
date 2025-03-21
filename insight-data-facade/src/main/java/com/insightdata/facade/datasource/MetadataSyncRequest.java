package com.insightdata.facade.datasource;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request for syncing metadata from data source
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetadataSyncRequest {
    private String type;
    private boolean fullSync;
    private String[] schemas;
    private String[] tables;
    private Map<String, Object> parameters;
    private boolean includeViews;
    private boolean includeProcedures;
    private boolean includeFunctions;
    private boolean includeIndexes;
    private boolean includeForeignKeys;
    private boolean includeConstraints;
    private boolean includePrivileges;
}