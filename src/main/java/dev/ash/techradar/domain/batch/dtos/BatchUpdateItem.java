package dev.ash.techradar.domain.batch.dtos;

import dev.ash.techradar.domain.technology.dtos.UpdateTechnologyRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class BatchUpdateItem {

    @NotNull
    private UUID id;

    private UpdateTechnologyRequest updates;
}
