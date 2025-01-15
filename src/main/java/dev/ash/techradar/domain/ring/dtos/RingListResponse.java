package dev.ash.techradar.domain.ring.dtos;

import lombok.Data;

import java.util.List;

@Data
public class RingListResponse {

    private List<RingSummaryResponse> rings;
}
