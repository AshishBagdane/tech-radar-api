package dev.ash.techradar.domain.ring.services;

import dev.ash.techradar.domain.ring.dtos.RingListResponse;
import dev.ash.techradar.domain.ring.dtos.RingTechnologyResponse;
import dev.ash.techradar.domain.technology.dtos.TechnologyFilter;
import dev.ash.techradar.domain.technology.enums.Ring;
import java.util.Map;

public interface RingService {

  RingListResponse getAllRings(TechnologyFilter filter);

  RingTechnologyResponse getTechnologiesForRing(Ring ringType, TechnologyFilter filter);

  Map<Ring, Long> getDistributionByRing();
}