package dev.ash.techradar.common.specifications;

import jakarta.persistence.criteria.Predicate;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BaseSpecificationBuilder<T> {

    private final List<Function<BuildContext<T>, Specification<T>>> specifications = new ArrayList<>();

    public BaseSpecificationBuilder<T> with(Function<BuildContext<T>, Specification<T>> specificationFn) {
        specifications.add(specificationFn);
        return this;
    }

    public BaseSpecificationBuilder<T> withIfPresent(Object value,
                                                     Function<BuildContext<T>, Specification<T>> specificationFn) {
        if (value != null) {
            specifications.add(specificationFn);
        }
        return this;
    }

    public Specification<T> build() {
        if (specifications.isEmpty()) {
            return Specification.where(null);
        }

        BuildContext<T> context = new BuildContext<>();

        return specifications.stream()
            .map(specFn -> specFn.apply(context))
            .reduce(Specification.where(null), Specification::and);
    }

    // Context class to share state between specifications if needed
    @Getter
    public static class BuildContext<T> {

        private final List<Predicate> predicates = new ArrayList<>();

        public void addPredicate(Predicate predicate) {
            predicates.add(predicate);
        }

    }
}
