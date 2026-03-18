package dev.siqueira.redis_cache_gaming.dtos;

import lombok.Builder;

@Builder
public record UserResponseDto(String username, String email, Long points) {
}
