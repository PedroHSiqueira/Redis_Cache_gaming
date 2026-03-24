package dev.siqueira.redis_cache_gaming.service;

import dev.siqueira.redis_cache_gaming.dtos.PointsRequestDto;
import dev.siqueira.redis_cache_gaming.dtos.PointsResponseDto;
import dev.siqueira.redis_cache_gaming.entity.User;
import dev.siqueira.redis_cache_gaming.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PointsServiceTest {

    @InjectMocks
    private PointsService pointsService;

    @Mock
    private UserRepository userRepository;

    @Test
    void plusPointsSuccessfully() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setUsername("Pedro");
        user.setEmail("Siqueira@gmail.com");
        user.setPoints(0L);

        PointsRequestDto request = new PointsRequestDto(1L);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PointsResponseDto response = pointsService.plusPoints(userId, request);

        assertNotNull(response);
        assertEquals("Pedro", response.username());
        assertEquals(1L, response.newPoints());

        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    void plusPointsFailure() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> userDB = userRepository.findById(1L);

        assertEquals(Optional.empty(), userDB);

        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }
}