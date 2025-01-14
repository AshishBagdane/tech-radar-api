package dev.ash.techradar.domain.ring.api;

import dev.ash.techradar.domain.ring.dtos.RingListResponse;
import dev.ash.techradar.domain.ring.dtos.RingTechnologyResponse;
import dev.ash.techradar.domain.ring.services.RingService;
import dev.ash.techradar.domain.technology.dtos.TechnologyFilter;
import dev.ash.techradar.domain.technology.enums.Ring;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rings")
@RequiredArgsConstructor
@Validated
@Tag(name = "Ring Management", description = "APIs for managing technology rings")
public class RingController {

    private final RingService ringService;

    @GetMapping
    @Operation(summary = "List all rings with their technologies")
    public ResponseEntity<RingListResponse> getAllRings(
        @Parameter(description = "Filter criteria for technologies")
        @ModelAttribute TechnologyFilter filter) {

        return ResponseEntity.ok(ringService.getAllRings(filter));
    }

    @GetMapping("/{ringType}/technologies")
    @Operation(summary = "Get technologies for specific ring")
    public ResponseEntity<RingTechnologyResponse> getTechnologiesForRing(
        @Parameter(description = "Ring type (ADOPT, TRIAL, ASSESS, HOLD)")
        @PathVariable Ring ringType,
        @Parameter(description = "Filter criteria for technologies")
        @ModelAttribute TechnologyFilter filter) {

        return ResponseEntity.ok(ringService.getTechnologiesForRing(ringType, filter));
    }
}
