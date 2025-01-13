package dev.ash.techradar.domain.ring.dtos;

import java.util.List;
import lombok.Data;

@Data
public class RingListResponse {

  private List<RingSummaryResponse> rings;
}