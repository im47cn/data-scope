package com.insightdata.application.service;

public interface MetadataSyncApplicationService {
    /**
     * 同步数据源的元数据
     *
     * @param dataSourceId 数据源ID
     */
    void syncMetadata(String dataSourceId);
}