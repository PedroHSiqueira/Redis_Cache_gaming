package dev.siqueira.redis_cache_gaming.service;

import dev.siqueira.redis_cache_gaming.exception.TooManyRequestsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;


@ExtendWith(MockitoExtension.class)
class RateLimitServiceTest {

    @InjectMocks
    private RateLimitService rateLimitService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @BeforeEach
    void setup() {
        Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("Redis dosen't encounter this user on database")
    void rateLimitWithNullCount() {
        Long userId = 1L;
        String key = "rate_limit:user:" + userId;

        Mockito.when(valueOperations.increment(key)).thenReturn(null);

        rateLimitService.rateLimit(userId);

        Mockito.verify(redisTemplate, Mockito.never()).expire(Mockito.anyString(), Mockito.any());
    }

    @Test
    @DisplayName("Redis encounter this user on database, and is his firts time")
    void rateLimitFirstCall() {
        Long userId = 1L;
        String key = "rate_limit:user:" + userId;

        Mockito.when(valueOperations.increment(key)).thenReturn(1L);

        rateLimitService.rateLimit(userId);

        Mockito.verify(redisTemplate).expire(Mockito.eq(key), Mockito.eq(Duration.ofSeconds(60)));
    }

    @Test
    @DisplayName("Normal Routine on rate limit")
    void rateLimitSecondCall() {
        Long userId = 1L;
        String key = "rate_limit:user:" + userId;

        Mockito.when(valueOperations.increment(key)).thenReturn(3L);

        rateLimitService.rateLimit(userId);

        Mockito.verify(redisTemplate, Mockito.never()).expire(Mockito.anyString(), Mockito.any());
    }

    @Test
    @DisplayName("Too many requests on Redis")
    void rateLimitTooManyCall() {
        Long userId = 1L;
        String key = "rate_limit:user:" + userId;

        Mockito.when(valueOperations.increment(key)).thenReturn(6L);

        TooManyRequestsException ex = Assertions.assertThrows(
                TooManyRequestsException.class,
                () -> rateLimitService.rateLimit(userId)
        );

        Assertions.assertTrue(ex.getMessage().contains("Muitos chamados"));

        Mockito.verify(redisTemplate, Mockito.never()).expire(Mockito.anyString(), Mockito.any());}
}