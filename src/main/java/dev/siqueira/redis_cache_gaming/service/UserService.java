package dev.siqueira.redis_cache_gaming.service;

import dev.siqueira.redis_cache_gaming.dtos.RankingResponseDto;
import dev.siqueira.redis_cache_gaming.dtos.UserRequestDto;
import dev.siqueira.redis_cache_gaming.entity.User;
import dev.siqueira.redis_cache_gaming.repository.UserRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


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

    public List<RankingResponseDto> getRanking(int limit){
        this.populateRedis();

        Set<ZSetOperations.TypedTuple<String>> result = redisTemplate.opsForZSet()
                .reverseRangeWithScores("ranking", 0, limit - 1);

        if (result == null || result.isEmpty()) {
            return List.of();
        }

        List<RankingResponseDto> ranking = new ArrayList<>();

        int position = 1;

        for (ZSetOperations.TypedTuple<String> tuple : result) {
            String username = tuple.getValue();
            Double score = tuple.getScore();
            long points = (score != null) ? score.longValue() : 0;

            ranking.add(new RankingResponseDto(username, points, position++));

        }

        return ranking;
    }

    public void populateRedis (){
        userRepository.findAll().forEach(user -> {
            redisTemplate.opsForZSet().add("ranking", user.getUsername(), user.getPoints());});
    }

    public void delete(Long id){
        userRepository.findById(id).ifPresent(userRepository::delete);
    }
}
