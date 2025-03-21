package com.insightdata.controller;

import com.insightdata.application.service.MetadataSyncJobApplicationService;
import com.insightdata.domain.metadata.model.MetadataSyncJob;
import com.insightdata.facade.metadata.CreateMetadataSyncJobRequest;
import com.insightdata.facade.metadata.MetadataSyncJobDTO;
import com.insightdata.facade.metadata.enums.SyncStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 元数据同步作业控制器
 */
@RestController
@RequestMapping("/api/v1/metadata-sync-jobs")
public class MetadataSyncJobController {

    private final MetadataSyncJobApplicationService metadataSyncJobApplicationService;

    @Autowired
    public MetadataSyncJobController(MetadataSyncJobApplicationService metadataSyncJobApplicationService) {
        this.metadataSyncJobApplicationService = metadataSyncJobApplicationService;
    }

    /**
     * 创建同步作业
     *
     * @param request 创建同步作业请求
     * @return 创建的同步作业
     */
    @PostMapping
    public ResponseEntity<MetadataSyncJobDTO> createSyncJob(@Valid @RequestBody CreateMetadataSyncJobRequest request) {
        MetadataSyncJob syncJob = metadataSyncJobApplicationService.createSyncJob(request.getDataSourceId(), request.getType());
        return ResponseEntity.status(HttpStatus.CREATED).body(MetadataSyncJobDTO.fromDomain(syncJob));
    }

    /**
     * 启动同步作业
     *
     * @param jobId 同步作业ID
     * @return 启动后的同步作业
     */
    @PostMapping("/{jobId}/start")
    public ResponseEntity<MetadataSyncJobDTO> startSyncJob(@PathVariable String jobId) {
        MetadataSyncJob syncJob = metadataSyncJobApplicationService.startSyncJob(jobId);
        return ResponseEntity.ok(MetadataSyncJobDTO.fromDomain(syncJob));
    }

    /**
     * 更新同步作业进度
     *
     * @param jobId    同步作业ID
     * @param progress 进度值
     * @param message  进度消息(可选)
     * @return 更新后的同步作业
     */
    @PutMapping("/{jobId}/progress")
    public ResponseEntity<MetadataSyncJobDTO> updateProgress(
            @PathVariable String jobId,
            @RequestParam int progress,
            @RequestParam(required = false) String message) {
        MetadataSyncJob syncJob = metadataSyncJobApplicationService.updateProgress(jobId, progress, message);
        return ResponseEntity.ok(MetadataSyncJobDTO.fromDomain(syncJob));
    }

    /**
     * 完成同步作业
     *
     * @param jobId 同步作业ID
     * @return 更新后的同步作业
     */
    @PostMapping("/{jobId}/complete")
    public ResponseEntity<MetadataSyncJobDTO> completeSyncJob(@PathVariable String jobId) {
        MetadataSyncJob syncJob = metadataSyncJobApplicationService.completeSyncJob(jobId);
        return ResponseEntity.ok(MetadataSyncJobDTO.fromDomain(syncJob));
    }

    /**
     * 标记同步作业为失败
     *
     * @param jobId        同步作业ID
     * @param errorMessage 错误信息
     * @return 更新后的同步作业
     */
    @PostMapping("/{jobId}/fail")
    public ResponseEntity<MetadataSyncJobDTO> failSyncJob(
            @PathVariable String jobId,
            @RequestParam String errorMessage) {
        MetadataSyncJob syncJob = metadataSyncJobApplicationService.failSyncJob(jobId, errorMessage);
        return ResponseEntity.ok(MetadataSyncJobDTO.fromDomain(syncJob));
    }

    /**
     * 取消同步作业
     *
     * @param jobId 同步作业ID
     * @return 更新后的同步作业
     */
    @PostMapping("/{jobId}/cancel")
    public ResponseEntity<MetadataSyncJobDTO> cancelSyncJob(@PathVariable String jobId) {
        MetadataSyncJob syncJob = metadataSyncJobApplicationService.cancelSyncJob(jobId);
        return ResponseEntity.ok(MetadataSyncJobDTO.fromDomain(syncJob));
    }

    /**
     * 获取同步作业详情
     *
     * @param jobId 同步作业ID
     * @return 同步作业详情
     */
    @GetMapping("/{jobId}")
    public ResponseEntity<MetadataSyncJobDTO> getSyncJob(@PathVariable String jobId) {
        return metadataSyncJobApplicationService.getSyncJob(jobId)
                .map(syncJob -> ResponseEntity.ok(MetadataSyncJobDTO.fromDomain(syncJob)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 获取数据源的同步作业列表
     *
     * @param dataSourceId 数据源ID
     * @return 同步作业列表
     */
    @GetMapping("/datasource/{dataSourceId}")
    public ResponseEntity<List<MetadataSyncJobDTO>> getSyncJobsByDataSource(@PathVariable String dataSourceId) {
        List<MetadataSyncJob> syncJobs = metadataSyncJobApplicationService.getSyncJobsByDataSource(dataSourceId);
        List<MetadataSyncJobDTO> dtos = syncJobs.stream()
                .map(MetadataSyncJobDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * 获取数据源的最新同步作业
     *
     * @param dataSourceId 数据源ID
     * @return 最新同步作业
     */
    @GetMapping("/datasource/{dataSourceId}/latest")
    public ResponseEntity<MetadataSyncJobDTO> getLatestSyncJob(@PathVariable String dataSourceId) {
        return metadataSyncJobApplicationService.getLatestSyncJob(dataSourceId)
                .map(syncJob -> ResponseEntity.ok(MetadataSyncJobDTO.fromDomain(syncJob)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 获取指定状态的同步作业列表
     *
     * @param status 同步状态
     * @return 同步作业列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<MetadataSyncJobDTO>> getSyncJobsByStatus(@PathVariable SyncStatus status) {
        List<MetadataSyncJob> syncJobs = metadataSyncJobApplicationService.getSyncJobsByStatus(status);
        List<MetadataSyncJobDTO> dtos = syncJobs.stream()
                .map(MetadataSyncJobDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * 获取正在进行中的同步作业列表
     *
     * @return 同步作业列表
     */
    @GetMapping("/in-progress")
    public ResponseEntity<List<MetadataSyncJobDTO>> getInProgressSyncJobs() {
        List<MetadataSyncJob> syncJobs = metadataSyncJobApplicationService.getInProgressSyncJobs();
        List<MetadataSyncJobDTO> dtos = syncJobs.stream()
                .map(MetadataSyncJobDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}