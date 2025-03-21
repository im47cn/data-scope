package com.insightdata.facade.datasource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Statistics about data source metadata
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetadataStats {
    private int schemaCount;
    private int tableCount;
    private int viewCount;
    private int procedureCount;
    private int functionCount;
    private int columnCount;
    private int indexCount;
    private int foreignKeyCount;
    private int constraintCount;
    private int privilegeCount;
    private long totalRowCount;
    private long totalStorageSize;
}