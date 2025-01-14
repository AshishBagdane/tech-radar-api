package dev.ash.techradar.domain.technology.mappers;

import dev.ash.techradar.domain.technology.dtos.CreateTechnologyRequest;
import dev.ash.techradar.domain.technology.dtos.TechnologyResponse;
import dev.ash.techradar.domain.technology.dtos.UpdateTechnologyRequest;
import dev.ash.techradar.domain.technology.entities.Technology;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
    componentModel = "spring"
)
public interface TechnologyMapper {

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Technology toEntity(CreateTechnologyRequest request);

    TechnologyResponse toResponse(Technology technology);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(@MappingTarget Technology technology, UpdateTechnologyRequest request);
}
