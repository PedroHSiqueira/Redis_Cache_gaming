package dev.siqueira.redis_cache_gaming.mapper;

import dev.siqueira.redis_cache_gaming.dtos.UserRequestDto;
import dev.siqueira.redis_cache_gaming.dtos.UserResponseDto;
import dev.siqueira.redis_cache_gaming.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Mock
    private User user;

    @Mock
    private UserRequestDto userRequestDto;

    @Mock
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("Pedro");
        user.setEmail("Siqueira@gmail.com");

        userRequestDto = new UserRequestDto("Siqueira", "pedro@gmail.com");

    }

    @Test
    @DisplayName("Convert UserRequestDto to User")
    void toEntity() {
        User result = UserMapper.toEntity(userRequestDto);

        assertNotNull(result);
        assertEquals("Siqueira", result.getUsername());
        assertEquals("pedro@gmail.com", result.getEmail());
    }

    @Test
    @DisplayName("Convert User to UserRequestDto")
    void toUserResponseDto() {
        UserResponseDto userDto = UserMapper.toUserResponseDto(user);

        assertNotNull(userDto);
        assertEquals("Pedro", userDto.username());
        assertEquals("Siqueira@gmail.com", userDto.email());
    }
}