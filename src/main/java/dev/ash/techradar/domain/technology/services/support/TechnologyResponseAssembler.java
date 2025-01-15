package dev.ash.techradar.domain.technology.services.support;

import dev.ash.techradar.domain.technology.dtos.CreateTechnologyRequest;
import dev.ash.techradar.domain.technology.dtos.PageMetadata;
import dev.ash.techradar.domain.technology.dtos.TechnologyListResponse;
import dev.ash.techradar.domain.technology.dtos.TechnologyResponse;
import dev.ash.techradar.domain.technology.dtos.UpdateTechnologyRequest;
import dev.ash.techradar.domain.technology.entities.Technology;
import dev.ash.techradar.domain.technology.mappers.TechnologyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TechnologyResponseAssembler {

    private final TechnologyMapper technologyMapper;

    public TechnologyResponse toResponse(Technology technology) {
        return technologyMapper.toResponse(technology);
    }

    public Technology toEntity(CreateTechnologyRequest request) {
        return technologyMapper.toEntity(request);
    }

    public void updateEntity(Technology technology, UpdateTechnologyRequest request) {
        technologyMapper.updateEntity(technology, request);
    }

    public TechnologyListResponse createListResponse(Page<Technology> page) {
        List<TechnologyResponse> technologies = page.getContent().stream()
            .map(technologyMapper::toResponse)
            .toList();

        TechnologyListResponse response = new TechnologyListResponse();
        response.setContent(technologies);
        response.setPage(createPageMetadata(page));

        return response;
    }

    private PageMetadata createPageMetadata(Page<?> page) {
        PageMetadata metadata = new PageMetadata();
        metadata.setNumber(page.getNumber());
        metadata.setSize(page.getSize());
        metadata.setTotalElements(page.getTotalElements());
        metadata.setTotalPages(page.getTotalPages());
        return metadata;
    }
}
