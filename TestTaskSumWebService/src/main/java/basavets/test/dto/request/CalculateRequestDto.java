package basavets.test.dto.request;

import lombok.Builder;

@Builder(toBuilder = true)
public record CalculateRequestDto(String first, String second) {
}
