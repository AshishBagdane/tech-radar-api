package dev.ash.techradar.domain.quadrant.api;

import dev.ash.techradar.domain.quadrant.dtos.QuadrantListResponse;
import dev.ash.techradar.domain.quadrant.dtos.QuadrantTechnologyResponse;
import dev.ash.techradar.domain.quadrant.services.QuadrantService;
import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/quadrants")
@RequiredArgsConstructor
@Validated
@Tag(name = "Quadrant Management", description = "APIs for managing quadrants and their technologies")
public class QuadrantController {

    private final QuadrantService quadrantService;

    @GetMapping
    @Operation(summary = "List all quadrants with their technologies")
    public QuadrantListResponse getAllQuadrants(
        @RequestParam(required = false) Ring ring
    ) {
        return quadrantService.getAllQuadrants(Optional.ofNullable(ring));
    }

    @GetMapping("/{quadrantType}/technologies")
    @Operation(summary = "Get technologies for specific quadrant")
    public QuadrantTechnologyResponse getTechnologiesByQuadrant(
        @PathVariable Quadrant quadrantType,
        @RequestParam(required = false) Ring ring,
        @RequestParam(required = false) String search
    ) {
        return quadrantService.getTechnologiesByQuadrant(
            quadrantType,
            Optional.ofNullable(ring),
            Optional.ofNullable(search)
        );
    }
}
