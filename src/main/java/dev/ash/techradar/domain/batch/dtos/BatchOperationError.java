package dev.ash.techradar.domain.batch.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class BatchOperationError {

    private UUID technologyId;

    private String errorMessage;

    private String errorCode;
}
