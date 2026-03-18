package dev.siqueira.redis_cache_gaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RedisCacheGamingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisCacheGamingApplication.class, args);
    }

}
