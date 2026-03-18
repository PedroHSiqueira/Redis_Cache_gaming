package dev.siqueira.redis_cache_gaming.dtos;

import lombok.Builder;

@Builder
public record PointsResponseDto(String username, Long newPoints) {
}
