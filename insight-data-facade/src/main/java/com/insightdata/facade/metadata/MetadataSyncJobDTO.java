package com.insightdata.facade.metadata;

import com.insightdata.facade.metadata.enums.SyncStatus;
import com.insightdata.facade.metadata.enums.SyncType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MetadataSyncJobDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String dataSourceId;
    private SyncType type;
    private SyncStatus status;
    private Integer progress;
    private Integer totalItems;
    private Integer processedItems;
    private Map<String, Object> parameters;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String typeDisplayName;
    private String statusDisplayName;
    private Long executionDuration;

    public Map<String, Object> getParameters() {
        return parameters != null ? Collections.unmodifiableMap(parameters) : null;
    }

    @SuppressWarnings("unchecked")
    public static MetadataSyncJobDTO fromDomain(Object syncJob) {
        if (syncJob == null) {
            return null;
        }

        try {

            // Use reflection to access properties of the domain object
            MetadataSyncJobDTO defaultDTO = new MetadataSyncJobDTO();
            MetadataSyncJobDTOBuilder builder = defaultDTO.toBuilder();

            String idVal = invokeGetter(syncJob, "getId", String.class);
            String dataSourceIdVal = invokeGetter(syncJob, "getDataSourceId", String.class);
            SyncType typeVal = (SyncType) invokeGetter(syncJob, "getType", SyncType.class);
            SyncStatus statusVal = invokeGetter(syncJob, "getStatus", SyncStatus.class);
            Integer progressVal = invokeGetter(syncJob, "getProgress", Integer.class);
            Integer totalItemsVal = invokeGetter(syncJob, "getTotalItems", Integer.class);
            Integer processedItemsVal = invokeGetter(syncJob, "getProcessedItems", Integer.class);
            Map<String, Object> parametersVal = (Map<String, Object>) invokeGetter(syncJob, "getParameters", Map.class);
            LocalDateTime startTimeVal = invokeGetter(syncJob, "getStartTime", LocalDateTime.class);
            LocalDateTime endTimeVal = invokeGetter(syncJob, "getEndTime", LocalDateTime.class);
            String errorMessageVal = invokeGetter(syncJob, "getErrorMessage", String.class);
            LocalDateTime createdAtVal = invokeGetter(syncJob, "getCreatedAt", LocalDateTime.class);
            LocalDateTime updatedAtVal = invokeGetter(syncJob, "getUpdatedAt", LocalDateTime.class);
            String typeDisplayNameVal = invokeGetter(syncJob, "getTypeDisplayName", String.class);
            String statusDisplayNameVal = invokeGetter(syncJob, "getStatusDisplayName", String.class);
            Long executionDurationVal = invokeGetter(syncJob, "getExecutionDuration", Long.class);

           return builder.id(idVal)
                    .dataSourceId(dataSourceIdVal)
                    .type(typeVal)
                    .status(statusVal)
                    .progress(progressVal)
                    .totalItems(totalItemsVal)
                    .processedItems(processedItemsVal)
                    .parameters(parametersVal)
                    .startTime(startTimeVal)
                    .endTime(endTimeVal)
                    .errorMessage(errorMessageVal)
                    .createdAt(createdAtVal)
                    .updatedAt(updatedAtVal)
                    .typeDisplayName(typeDisplayNameVal)
                    .statusDisplayName(statusDisplayNameVal)
                    .executionDuration(executionDurationVal)
                    .build();
        } catch (Exception e) {
            System.err.println("Error converting domain object to DTO: " + e.getMessage());
            return null;
        }
    }

    private static <T> T invokeGetter(Object obj, String methodName, Class<T> returnType) {
        try {
            return (T) obj.getClass().getMethod(methodName).invoke(obj);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.err.println("Error invoking getter " + methodName + ": " + e.getMessage());
            return null;
        }
    }
}