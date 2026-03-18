package dev.siqueira.redis_cache_gaming.mapper;

import dev.siqueira.redis_cache_gaming.dtos.UserRequestDto;
import dev.siqueira.redis_cache_gaming.dtos.UserResponseDto;
import dev.siqueira.redis_cache_gaming.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public User toEntity(UserRequestDto userRequestDto) {
        User user = new User();
        user.setUsername(userRequestDto.username());
        user.setEmail(userRequestDto.email());
        return user;
    }

    public UserResponseDto toUserResponseDto(User user) {
        return UserResponseDto.builder().username(user.getUsername()).email(user.getEmail()).points(user.getPoints()).build();
    }
}
