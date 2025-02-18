package dev.ash.techradar.domain.technology.mappers;

import dev.ash.techradar.domain.technology.dtos.CreateTechnologyRequest;
import dev.ash.techradar.domain.technology.dtos.TechnologyResponse;
import dev.ash.techradar.domain.technology.dtos.UpdateTechnologyRequest;
import dev.ash.techradar.domain.technology.entities.Technology;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface TechnologyMapper {

    @Mapping(target = "originalState", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "quadrant", source = "quadrant")
    @Mapping(target = "ring", source = "ring")
    Technology toEntity(CreateTechnologyRequest request);

    @AfterMapping
    default void afterToEntity(@MappingTarget Technology target, CreateTechnologyRequest source) {
        if (source.getMetadata() != null) {
            target.getMetadata().putAll(source.getMetadata());
        }
    }

    TechnologyResponse toResponse(Technology technology);

    @Mapping(target = "originalState", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "quadrant", source = "quadrant")
    @Mapping(target = "ring", source = "ring")
    void updateEntity(@MappingTarget Technology technology, UpdateTechnologyRequest request);

    @AfterMapping
    default void afterUpdateEntity(@MappingTarget Technology target, UpdateTechnologyRequest source) {
        if (source.getMetadata() != null) {
            target.getMetadata().clear();
            target.getMetadata().putAll(source.getMetadata());
        }
    }
}
