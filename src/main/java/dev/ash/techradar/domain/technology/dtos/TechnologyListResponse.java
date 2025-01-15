package dev.ash.techradar.domain.technology.dtos;

import lombok.Data;

import java.util.List;

@Data
public class TechnologyListResponse {

    private List<TechnologyResponse> content;

    private dev.ash.techradar.domain.technology.dtos.PageMetadata page;
}
