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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuadrantServiceImpl implements QuadrantService {

  private final TechnologyRepository technologyRepository;
  private final TechnologyMapper technologyMapper;

  @Override
  public QuadrantListResponse getAllQuadrants(Optional<Ring> ringFilter) {
    List<Technology> technologies = ringFilter
        .map(technologyRepository::findByRing)
        .orElseGet(technologyRepository::findAll);

    Map<Quadrant, List<Technology>> techsByQuadrant = technologies.stream()
        .collect(Collectors.groupingBy(Technology::getQuadrant));

    List<QuadrantSummaryResponse> summaries = Arrays.stream(Quadrant.values())
        .map(quadrant -> createQuadrantSummary(quadrant, techsByQuadrant.getOrDefault(quadrant, new ArrayList<>())))
        .collect(Collectors.toList());

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
    List<Technology> technologies = technologyRepository.findByQuadrantAndFilters(
        quadrantType,
        ringFilter.orElse(null),
        search.orElse(null)
    );

    QuadrantTechnologyResponse response = new QuadrantTechnologyResponse();
    response.setQuadrantType(quadrantType);
    response.setTechnologies(technologies.stream()
        .map(technologyMapper::toResponse)
        .collect(Collectors.toList()));
    response.setTotalTechnologies(technologies.size());

    return response;
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