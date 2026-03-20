package dev.siqueira.redis_cache_gaming.controller;

import dev.siqueira.redis_cache_gaming.dtos.*;
import dev.siqueira.redis_cache_gaming.entity.User;
import dev.siqueira.redis_cache_gaming.exception.TooManyRequestsException;
import dev.siqueira.redis_cache_gaming.mapper.UserMapper;
import dev.siqueira.redis_cache_gaming.service.PointsService;
import dev.siqueira.redis_cache_gaming.service.RateLimitService;
import dev.siqueira.redis_cache_gaming.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PointsService pointsService;
    private final RateLimitService rateLimitService;

    public UserController(UserService userService, PointsService pointsService, RateLimitService rateLimitService) {
        this.userService = userService;
        this.pointsService = pointsService;
        this.rateLimitService = rateLimitService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        try {
            rateLimitService.rateLimit(id);
            User userDB = userService.findById(id);

            if (userDB == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toUserResponseDto(userDB));
        }catch (TooManyRequestsException e){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(e.getMessage());
        }
    }

    @GetMapping("/ranking/{limit}")
    public ResponseEntity<List<RankingResponseDto>> getRanking(@PathVariable int limit){
        List<RankingResponseDto> ranking = userService.getRanking(limit);

        if(ranking == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(ranking);
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody UserRequestDto userRequestDto){
        User userSaved = userService.save(UserMapper.toEntity(userRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
    }

    @PostMapping("/{id}/score")
    private ResponseEntity<PointsResponseDto> plusPoints(@PathVariable Long id, @RequestBody PointsRequestDto pointsRequestDto){
        PointsResponseDto pointsResponseDto = pointsService.plusPoints(id, pointsRequestDto);

        if(pointsResponseDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(pointsResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto){
        User userUpdated = userService.update(id, userRequestDto);

        if(userUpdated == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toUserResponseDto(userUpdated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDto> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
