package dev.ash.techradar.domain.batch.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class BatchUpdateRequest {

    @NotNull
    @Size(min = 1, max = 100)
    private List<BatchUpdateItem> technologies;
}
