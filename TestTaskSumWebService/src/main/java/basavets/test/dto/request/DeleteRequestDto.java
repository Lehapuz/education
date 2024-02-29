package basavets.test.dto.request;

import lombok.Builder;

@Builder(toBuilder = true)
public record DeleteRequestDto(String name) {
}
