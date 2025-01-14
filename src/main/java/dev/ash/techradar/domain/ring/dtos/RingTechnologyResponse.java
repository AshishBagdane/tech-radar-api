package dev.ash.techradar.domain.ring.dtos;

import dev.ash.techradar.domain.technology.dtos.TechnologyResponse;
import dev.ash.techradar.domain.technology.enums.Ring;
import lombok.Data;

import java.util.List;

@Data
public class RingTechnologyResponse {

    private Ring ringType;

    private List<TechnologyResponse> technologies;

    private int totalTechnologies;
}
