package dev.ash.techradar.domain.technology.dtos;

import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
public class UpdateTechnologyRequest {

    @Size(min = 1, max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    private Quadrant quadrant;

    private Ring ring;

    private Map<String, String> metadata;
}
