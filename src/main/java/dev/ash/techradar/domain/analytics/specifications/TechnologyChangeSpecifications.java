package dev.ash.techradar.domain.analytics.specifications;

import dev.ash.techradar.domain.technology.entities.Technology;
import dev.ash.techradar.domain.technology.entities.TechnologyChange;
import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import jakarta.persistence.criteria.Join;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TechnologyChangeSpecifications {

    public static Specification<TechnologyChange> changeDateAfter(LocalDateTime since) {
        return (root, query, cb) -> {
            if (since == null) {
                return null;
            }
            return cb.greaterThanOrEqualTo(root.get("changeDate"), since);
        };
    }

    public static Specification<TechnologyChange> withQuadrant(Quadrant quadrant) {
        return (root, query, cb) -> {
            if (quadrant == null) {
                return null;
            }
            Join<TechnologyChange, Technology> technology = root.join("technology");
            return cb.equal(technology.get("quadrant"), quadrant);
        };
    }

    public static Specification<TechnologyChange> withRing(Ring ring) {
        return (root, query, cb) -> {
            if (ring == null) {
                return null;
            }
            Join<TechnologyChange, Technology> technology = root.join("technology");
            return cb.equal(technology.get("ring"), ring);
        };
    }

    public static Specification<TechnologyChange> orderByChangeDate() {
        return (root, query, cb) -> {
            query.orderBy(cb.desc(root.get("changeDate")));
            return null;
        };
    }

    public static Specification<TechnologyChange> fetchTechnology() {
        return (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("technology");
            }
            return null;
        };
    }
}
