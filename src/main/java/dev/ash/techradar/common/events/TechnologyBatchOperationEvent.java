package dev.ash.techradar.common.events;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class TechnologyBatchOperationEvent {

    private String operationType;

    private Map<String, Object> operationDetails;

    private LocalDateTime operationTime;

    private int affectedCount;
}
