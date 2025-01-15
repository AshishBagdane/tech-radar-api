package dev.ash.techradar.domain.ring.services.impl;

import dev.ash.techradar.domain.ring.dtos.RingListResponse;
import dev.ash.techradar.domain.ring.dtos.RingSummaryResponse;
import dev.ash.techradar.domain.ring.dtos.RingTechnologyResponse;
import dev.ash.techradar.domain.ring.services.RingService;
import dev.ash.techradar.domain.technology.dtos.TechnologyFilter;
import dev.ash.techradar.domain.technology.dtos.TechnologyResponse;
import dev.ash.techradar.domain.technology.entities.Technology;
import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import dev.ash.techradar.domain.technology.repositories.TechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RingServiceImpl implements RingService {

    private final TechnologyRepository technologyRepository;

    @Override
    public RingListResponse getAllRings(TechnologyFilter filter) {
        RingListResponse response = new RingListResponse();
        List<RingSummaryResponse> ringsSummary = new ArrayList<>();

        for (Ring ring : Ring.values()) {
            RingSummaryResponse summary = new RingSummaryResponse();
            summary.setRingType(ring);

            // Get technologies distribution by quadrant for this ring
            Map<Quadrant, Integer> distributionByQuadrant = new EnumMap<>(Quadrant.class);
            for (Quadrant quadrant : Quadrant.values()) {
                int count = technologyRepository.countByRingAndQuadrant(ring, quadrant);
                distributionByQuadrant.put(quadrant, count);
            }

            summary.setTechnologiesByQuadrant(distributionByQuadrant);
            summary.setTotalTechnologies(distributionByQuadrant.values().stream()
                                             .mapToInt(Integer::intValue)
                                             .sum());

            ringsSummary.add(summary);
        }

        response.setRings(ringsSummary);
        return response;
    }

    @Override
    public RingTechnologyResponse getTechnologiesForRing(Ring ringType, TechnologyFilter filter) {
        List<Technology> technologies = technologyRepository.findByRingWithFilters(
            ringType,
            filter.getQuadrant(),
            filter.getSearch(),
            filter.getFromDate(),
            filter.getToDate()
        );

        RingTechnologyResponse response = new RingTechnologyResponse();
        response.setRingType(ringType);
        response.setTechnologies(technologies.stream()
                                     .map(this::mapToTechnologyResponse)
                                     .toList());
        response.setTotalTechnologies(technologies.size());

        return response;
    }

    @Override
    public Map<Ring, Long> getDistributionByRing() {
        Map<Ring, Long> distribution = new EnumMap<>(Ring.class);
        for (Ring ring : Ring.values()) {
            distribution.put(ring, technologyRepository.countByRing(ring));
        }
        return distribution;
    }

    private TechnologyResponse mapToTechnologyResponse(Technology technology) {
        TechnologyResponse response = new TechnologyResponse();
        response.setId(technology.getId());
        response.setName(technology.getName());
        response.setDescription(technology.getDescription());
        response.setQuadrant(technology.getQuadrant());
        response.setRing(technology.getRing());
        response.setCreatedAt(technology.getCreatedAt());
        response.setUpdatedAt(technology.getUpdatedAt());
        response.setMetadata(technology.getMetadata());
        return response;
    }
}
