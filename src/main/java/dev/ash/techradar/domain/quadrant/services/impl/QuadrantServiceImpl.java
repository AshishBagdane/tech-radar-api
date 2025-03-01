package dev.ash.techradar.domain.quadrant.services.impl;

import dev.ash.techradar.domain.quadrant.dtos.QuadrantListResponse;
import dev.ash.techradar.domain.quadrant.dtos.QuadrantSummaryResponse;
import dev.ash.techradar.domain.quadrant.dtos.QuadrantTechnologyResponse;
import dev.ash.techradar.domain.quadrant.services.QuadrantService;
import dev.ash.techradar.domain.technology.entities.Technology;
import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import dev.ash.techradar.domain.technology.mappers.TechnologyMapper;
import dev.ash.techradar.domain.technology.repositories.TechnologyRepository;
import dev.ash.techradar.domain.technology.specifications.TechnologySpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuadrantServiceImpl implements QuadrantService {

    private final TechnologyRepository technologyRepository;

    private final TechnologyMapper technologyMapper;

    private final TechnologySpecificationBuilder technologySpecificationBuilder;

    @Override
    public QuadrantListResponse getAllQuadrants(Optional<Ring> ringFilter) {
        List<Technology> technologies = ringFilter
            .map(technologyRepository::findByRing)
            .orElseGet(technologyRepository::findAll);

        Map<Quadrant, List<Technology>> techsByQuadrant = technologies.stream()
            .collect(Collectors.groupingBy(Technology::getQuadrant));

        List<QuadrantSummaryResponse> summaries = Arrays.stream(Quadrant.values())
            .map(quadrant -> createQuadrantSummary(quadrant, techsByQuadrant.getOrDefault(quadrant, new ArrayList<>())))
            .toList();

        QuadrantListResponse response = new QuadrantListResponse();
        response.setQuadrants(summaries);
        return response;
    }

    @Override
    public QuadrantTechnologyResponse getTechnologiesByQuadrant(
        Quadrant quadrantType,
        Optional<Ring> ringFilter,
        Optional<String> search
    ) {
        List<Technology> technologies = findByQuadrantAndFilters(
            quadrantType,
            ringFilter,
            search
        );

        QuadrantTechnologyResponse response = new QuadrantTechnologyResponse();
        response.setQuadrantType(quadrantType);
        response.setTechnologies(technologies.stream()
                                     .map(technologyMapper::toResponse)
                                     .toList());
        response.setTotalTechnologies(technologies.size());

        return response;
    }

    public List<Technology> findByQuadrantAndFilters(
        Quadrant quadrantType,
        Optional<Ring> ringFilter,
        Optional<String> search) {
        Specification<Technology> spec = technologySpecificationBuilder.buildQuadrantWithFiltersSpec(quadrantType,
                                                                                                     ringFilter,
                                                                                                     search);
        return technologyRepository.findAll(spec);
    }

    private QuadrantSummaryResponse createQuadrantSummary(Quadrant quadrant, List<Technology> technologies) {
        Map<Ring, Integer> techsByRing = technologies.stream()
            .collect(Collectors.groupingBy(
                Technology::getRing,
                Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
            ));

        QuadrantSummaryResponse summary = new QuadrantSummaryResponse();
        summary.setQuadrantType(quadrant);
        summary.setTechnologiesByRing(techsByRing);
        summary.setTotalTechnologies(technologies.size());
        return summary;
    }
}
