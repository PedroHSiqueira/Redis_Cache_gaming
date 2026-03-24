package dev.siqueira.redis_cache_gaming.service;

import dev.siqueira.redis_cache_gaming.dtos.PointsRequestDto;
import dev.siqueira.redis_cache_gaming.dtos.PointsResponseDto;
import dev.siqueira.redis_cache_gaming.entity.User;
import dev.siqueira.redis_cache_gaming.repository.UserRepository;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PointsService {

    private final UserRepository userRepository;

    public PointsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PointsResponseDto plusPoints(Long userId, @RequestBody PointsRequestDto dto){
        Optional<User> userDB = userRepository.findById(userId);

        if(userDB.isPresent()){
            User user = userDB.get();
            user.setPoints(user.getPoints() + dto.points());
            userRepository.save(user);

            return new PointsResponseDto(user.getUsername(), user.getPoints());
        }

        return null;
    }
}
