package dev.ash.techradar.domain.technology.specifications;

import dev.ash.techradar.domain.technology.entities.Technology;
import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TechnologySpecifications {

    public static Specification<Technology> withQuadrant(Quadrant quadrant) {
        return (root, query, cb) -> {
            if (quadrant == null) {
                return null;
            }
            return cb.equal(root.get("quadrant"), quadrant);
        };
    }

    public static Specification<Technology> withRing(Ring ring) {
        return (root, query, cb) -> {
            if (ring == null) {
                return null;
            }
            return cb.equal(root.get("ring"), ring);
        };
    }

    public static Specification<Technology> withSearch(String search) {
        return (root, query, cb) -> {
            if (search == null || search.trim().isEmpty()) {
                return null;
            }
            String likePattern = "%" + search.toLowerCase().trim() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("name")), likePattern),
                cb.like(cb.lower(root.get("description")), likePattern)
            );
        };
    }

    public static Specification<Technology> withNameLike(String searchTerm) {
        return (root, query, cb) -> {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return null;
            }
            String likePattern = "%" + searchTerm.toLowerCase().trim() + "%";
            return cb.like(cb.lower(root.get("name")), likePattern);
        };
    }

    public static Specification<Technology> createdBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        return (root, query, cb) -> {
            if (fromDate == null && toDate == null) {
                return null;
            }
            if (fromDate == null) {
                return cb.lessThanOrEqualTo(root.get("createdAt"), toDate);
            }
            if (toDate == null) {
                return cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate);
            }
            return cb.between(root.get("createdAt"), fromDate, toDate);
        };
    }

    public static Specification<Technology> updatedBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        return (root, query, cb) -> {
            if (fromDate == null && toDate == null) {
                return null;
            }
            if (fromDate == null) {
                return cb.lessThanOrEqualTo(root.get("updatedAt"), toDate);
            }
            if (toDate == null) {
                return cb.greaterThanOrEqualTo(root.get("updatedAt"), fromDate);
            }
            return cb.between(root.get("updatedAt"), fromDate, toDate);
        };
    }

    public static Specification<Technology> withExactName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.trim().isEmpty()) {
                return null;
            }
            return cb.equal(cb.lower(root.get("name")), name.toLowerCase().trim());
        };
    }

    public static Specification<Technology> withMetadataValue(String key, String value) {
        return (root, query, cb) -> {
            if (key == null || value == null) {
                return null;
            }

            Join<Technology, String> metadata = root.join("metadata", JoinType.INNER);
            return cb.and(
                cb.equal(metadata.get("metadata_key"), key),
                cb.equal(metadata.get("metadata_value"), value)
            );
        };
    }

}
