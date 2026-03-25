package dev.siqueira.redis_cache_gaming.service;

import dev.siqueira.redis_cache_gaming.dtos.PointsRequestDto;
import dev.siqueira.redis_cache_gaming.dtos.PointsResponseDto;
import dev.siqueira.redis_cache_gaming.entity.User;
import dev.siqueira.redis_cache_gaming.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    @Mock
    User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("Pedro");
        user.setEmail("Siqueira@gmail.com");
        user.setPoints(0L);
    }

    @Test
    @DisplayName("Should increment points on the user with success")
    void plusPointsSuccessfully() {
        Long userId = 1L;

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
    @DisplayName("Should not increment points on the user, because of an fail")
    void plusPointsFailure() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        userRepository.findById(1L);

        PointsResponseDto response = pointsService.plusPoints(1L, new PointsRequestDto(1L));

        assertNull(response);

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }
}