package dev.ash.techradar.domain.analytics.specifications;

import dev.ash.techradar.common.specifications.BaseSpecificationBuilder;
import dev.ash.techradar.domain.technology.entities.TechnologyChange;
import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TechnologyChangeSpecificationBuilder {

    public static Specification<TechnologyChange> buildRecentChangesSpec(
        LocalDateTime since,
        Quadrant quadrant,
        Ring ring,
        boolean fetchTechnology) {

        return new BaseSpecificationBuilder<TechnologyChange>()
            .withIfPresent(since,
                           ctx -> TechnologyChangeSpecifications.changeDateAfter(since))
            .withIfPresent(quadrant,
                           ctx -> TechnologyChangeSpecifications.withQuadrant(quadrant))
            .withIfPresent(ring,
                           ctx -> TechnologyChangeSpecifications.withRing(ring))
            .with(ctx -> TechnologyChangeSpecifications.orderByChangeDate())
            .withIfPresent(fetchTechnology && Boolean.TRUE,
                           ctx -> TechnologyChangeSpecifications.fetchTechnology())
            .build();
    }

    public static Specification<TechnologyChange> buildChangesByQuadrantSpec(
        Quadrant quadrant,
        LocalDateTime since) {

        return new BaseSpecificationBuilder<TechnologyChange>()
            .with(ctx -> TechnologyChangeSpecifications.changeDateAfter(since))
            .with(ctx -> TechnologyChangeSpecifications.withQuadrant(quadrant))
            .build();
    }
}
