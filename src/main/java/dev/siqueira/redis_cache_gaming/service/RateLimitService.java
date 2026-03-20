package dev.siqueira.redis_cache_gaming.service;

import dev.siqueira.redis_cache_gaming.exception.TooManyRequestsException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {

    private final RedisTemplate<String, String> redisTemplate;

    public RateLimitService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void rateLimit(Long userId) {
        String key = "rate_limit:user:" + userId;
        Long count = redisTemplate.opsForValue().increment(key);

        if (count == null) return;

        if (count == 1){
            redisTemplate.expire(key, Duration.ofSeconds(60));
        }

        if (count > 5){
            throw new TooManyRequestsException("Muitos chamados a API, espere um pouco para realizar novas requisições!");
        }
    }
}
