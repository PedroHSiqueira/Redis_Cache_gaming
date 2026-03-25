package dev.siqueira.redis_cache_gaming.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static org.junit.jupiter.api.Assertions.*;

class RedisConfigTest {

    private RedisConfig redisConfig;
    private RedisConnectionFactory connectionFactory;

    @BeforeEach
    void setUp() {
        redisConfig = new RedisConfig();
        connectionFactory = Mockito.mock(RedisConnectionFactory.class);
    }

    @Test
    void createRedisTemplateSuccessfully() {
        RedisTemplate<String, String> template = redisConfig.redisTemplate(connectionFactory);

        assertNotNull(template);
        assertEquals(connectionFactory, template.getConnectionFactory());

        assertInstanceOf(StringRedisSerializer.class, template.getKeySerializer());
        assertInstanceOf(StringRedisSerializer.class, template.getValueSerializer());
    }

    @Test
    void shouldNotReturnNullTemplate() {
        RedisTemplate<String, String> template = redisConfig.redisTemplate(connectionFactory);

        assertNotNull(template);
    }
}