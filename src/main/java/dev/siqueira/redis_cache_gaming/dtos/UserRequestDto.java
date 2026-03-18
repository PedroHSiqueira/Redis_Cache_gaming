package dev.siqueira.redis_cache_gaming.dtos;

import lombok.Builder;

@Builder
public record UserRequestDto(String username, String email) {
}
