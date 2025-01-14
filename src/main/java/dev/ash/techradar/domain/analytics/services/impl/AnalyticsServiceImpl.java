package dev.ash.techradar.domain.analytics.services.impl;

import dev.ash.techradar.domain.analytics.dtos.QuadrantMetricsResponse;
import dev.ash.techradar.domain.analytics.dtos.RadarChangesResponse;
import dev.ash.techradar.domain.analytics.dtos.RingMetricsResponse;
import dev.ash.techradar.domain.analytics.dtos.TechnologyChangeResponse;
import dev.ash.techradar.domain.analytics.repositories.TechnologyChangeRepository;
import dev.ash.techradar.domain.analytics.services.AnalyticsService;
import dev.ash.techradar.domain.technology.entities.Technology;
import dev.ash.techradar.domain.technology.entities.TechnologyChange;
import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import dev.ash.techradar.domain.technology.repositories.TechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class AnalyticsServiceImpl implements AnalyticsService {

    private final TechnologyRepository technologyRepository;

    private final TechnologyChangeRepository changeRepository;

    @Override
    @Transactional(readOnly = true)
    public QuadrantMetricsResponse getQuadrantMetrics() {
        List<Technology> technologies = technologyRepository.findAll();

        Map<Quadrant, Integer> distribution = technologies.stream()
            .collect(Collectors.groupingBy(
                Technology::getQuadrant,
                Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
            ));

        QuadrantMetricsResponse response = new QuadrantMetricsResponse();
        response.setDistributionByQuadrant(distribution);
        response.setTotalTechnologies(technologies.size());
        response.setLastUpdated(LocalDateTime.now());

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public RingMetricsResponse getRingMetrics() {
        List<Technology> technologies = technologyRepository.findAll();

        Map<Ring, Integer> distribution = technologies.stream()
            .collect(Collectors.groupingBy(
                Technology::getRing,
                Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
            ));

        RingMetricsResponse response = new RingMetricsResponse();
        response.setDistributionByRing(distribution);
        response.setTotalTechnologies(technologies.size());
        response.setLastUpdated(LocalDateTime.now());

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public RadarChangesResponse getRecentChanges(
        LocalDateTime since,
        Quadrant quadrant,
        Ring ring) {

        List<TechnologyChange> changes = changeRepository.findRecentChanges(since, quadrant, ring);

        List<TechnologyChangeResponse> changeResponses = changes.stream()
            .map(this::mapToChangeResponse)
            .toList();

        RadarChangesResponse response = new RadarChangesResponse();
        response.setChanges(changeResponses);
        response.setFromDate(since);
        response.setToDate(LocalDateTime.now());
        response.setTotalChanges(changeResponses.size());

        return response;
    }

    private TechnologyChangeResponse mapToChangeResponse(TechnologyChange change) {
        TechnologyChangeResponse response = new TechnologyChangeResponse();
        response.setTechnologyId(change.getTechnology().getId());
        response.setName(change.getTechnology().getName());
        response.setChangeType(change.getChangeType());
        response.setChangeDate(change.getChangeDate());
//        response.setChangedBy(change.getChangedBy());
        return response;
    }
}
