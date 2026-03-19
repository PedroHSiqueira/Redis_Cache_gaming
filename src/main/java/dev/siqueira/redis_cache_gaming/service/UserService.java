package dev.siqueira.redis_cache_gaming.service;

import dev.siqueira.redis_cache_gaming.dtos.UserRequestDto;
import dev.siqueira.redis_cache_gaming.entity.User;
import dev.siqueira.redis_cache_gaming.repository.UserRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository  userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public UserService(UserRepository userRepository, RedisTemplate<String, String> redisTemplate) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    public User save(User user){
        User userDB = userRepository.save(user);
        redisTemplate.opsForZSet().add("ranking", userDB.getId().toString(), userDB.getPoints());
        return userDB;
    }

    @Cacheable(value = "users", key = "#id", unless = "#result == null")
    public User findById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    @CachePut(value = "users", key = "#id", unless = "#result == null")
    public User update(Long id, UserRequestDto dto){
        User user = userRepository.findById(id).orElse(null);

        if (user != null){
            User newUser = new User();
            newUser.setId(user.getId());
            newUser.setUsername(dto.username());
            newUser.setEmail(dto.email());
            newUser.setPoints(user.getPoints());
            return userRepository.save(newUser);
        }

        return null;
    }

    public void delete(Long id){
        userRepository.findById(id).ifPresent(user -> {userRepository.delete(user);});
    }
}
