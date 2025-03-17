package com.facade.rest;

import com.application.service.MetadataSyncJobService;
import com.common.enums.SyncStatus;
import com.domain.model.metadata.MetadataSyncJob;
import com.facade.rest.dto.CreateMetadataSyncJobRequest;
import com.facade.rest.dto.MetadataSyncJobDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 元数据同步作业控制器
 */
@RestController
@RequestMapping("/api/metadata-sync-jobs")
public class MetadataSyncJobController {

    private final MetadataSyncJobService metadataSyncJobService;

    @Autowired
    public MetadataSyncJobController(MetadataSyncJobService metadataSyncJobService) {
        this.metadataSyncJobService = metadataSyncJobService;
    }

    /**
     * 创建同步作业
     *
     * @param request 创建同步作业请求
     * @return 创建的同步作业
     */
    @PostMapping
    public ResponseEntity<MetadataSyncJobDTO> createSyncJob(@Valid @RequestBody CreateMetadataSyncJobRequest request) {
        MetadataSyncJob syncJob = metadataSyncJobService.createSyncJob(request.getDataSourceId(), request.getType());
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
        MetadataSyncJob syncJob = metadataSyncJobService.startSyncJob(jobId);
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
        MetadataSyncJob syncJob = metadataSyncJobService.updateProgress(jobId, progress, message);
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
        MetadataSyncJob syncJob = metadataSyncJobService.completeSyncJob(jobId);
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
        MetadataSyncJob syncJob = metadataSyncJobService.failSyncJob(jobId, errorMessage);
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
        MetadataSyncJob syncJob = metadataSyncJobService.cancelSyncJob(jobId);
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
        return metadataSyncJobService.getSyncJob(jobId)
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
        List<MetadataSyncJob> syncJobs = metadataSyncJobService.getSyncJobsByDataSource(dataSourceId);
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
        return metadataSyncJobService.getLatestSyncJob(dataSourceId)
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
        List<MetadataSyncJob> syncJobs = metadataSyncJobService.getSyncJobsByStatus(status);
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
        List<MetadataSyncJob> syncJobs = metadataSyncJobService.getInProgressSyncJobs();
        List<MetadataSyncJobDTO> dtos = syncJobs.stream()
                .map(MetadataSyncJobDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}