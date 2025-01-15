package dev.ash.techradar.domain.technology.specifications;

import dev.ash.techradar.common.specifications.BaseSpecificationBuilder;
import dev.ash.techradar.domain.technology.dtos.TechnologyFilter;
import dev.ash.techradar.domain.technology.entities.Technology;
import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TechnologySpecificationBuilder {

    public Specification<Technology> createSpecification(TechnologyFilter filter) {
        return new BaseSpecificationBuilder<Technology>()
            .withIfPresent(filter.getQuadrant(),
                           ctx -> TechnologySpecifications.withQuadrant(filter.getQuadrant()))
            .withIfPresent(filter.getRing(),
                           ctx -> TechnologySpecifications.withRing(filter.getRing()))
            .withIfPresent(filter.getSearch(),
                           ctx -> TechnologySpecifications.withSearch(filter.getSearch()))
            .withIfPresent(filter.getFromDate(),
                           ctx -> TechnologySpecifications.updatedBetween(
                               filter.getFromDate(),
                               filter.getToDate()))
            .build();
    }

    public Specification<Technology> buildQuadrantWithFiltersSpec(
        Quadrant quadrant,
        Optional<Ring> ringFilter,
        Optional<String> search) {
        return new BaseSpecificationBuilder<Technology>()
            .with(ctx -> TechnologySpecifications.withQuadrant(quadrant))
            .withIfPresent(ringFilter.orElse(null),
                           ctx -> TechnologySpecifications.withRing(ringFilter.orElse(null)))
            .withIfPresent(search.orElse(null),
                           ctx -> TechnologySpecifications.withSearch(search.orElse(null)))
            .build();
    }

    public Specification<Technology> buildRingSpec(Ring ring) {
        return new BaseSpecificationBuilder<Technology>()
            .with(ctx -> TechnologySpecifications.withRing(ring))
            .build();
    }

    public Specification<Technology> buildNameSearchSpec(String name) {
        return new BaseSpecificationBuilder<Technology>()
            .with(ctx -> TechnologySpecifications.withExactName(name))
            .build();
    }

    public Specification<Technology> buildMetadataSearchSpec(String key, String value) {
        return new BaseSpecificationBuilder<Technology>()
            .with(ctx -> TechnologySpecifications.withMetadataValue(key, value))
            .build();
    }
}
