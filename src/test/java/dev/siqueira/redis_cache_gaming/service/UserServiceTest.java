package dev.siqueira.redis_cache_gaming.service;

import dev.siqueira.redis_cache_gaming.dtos.UserRequestDto;
import dev.siqueira.redis_cache_gaming.entity.User;
import dev.siqueira.redis_cache_gaming.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
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
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    User user;

    @Mock
    UserRequestDto userRequestDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("Pedro");
        user.setEmail("Siqueira@gmail.com");

        userRequestDto = new UserRequestDto("Siqueira", "pedro@gmail.com");
    }

    @Test
    @DisplayName("Save a User in the database with success")
    void saveUserSuccessfully() {
        Mockito.when(userRepository.save(user)).thenReturn(user);
        User result = userService.save(user);

        assertEquals(user, result);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    @DisplayName("Find by ID with success")
    void findByIdSuccessfully() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User result = userService.findById(1L);

        Assertions.assertEquals(user, result);
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("User not found in find by ID!")
    void findByIdNotFound() {
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.empty());
        User result = userService.findById(2L);

        Assertions.assertEquals(null, result);
        Mockito.verify(userRepository, Mockito.times(1)).findById(2L);
    }

    @Test
    @DisplayName("Delete from database by delete method")
    void deleteSuccessfully() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User user = userService.findById(1L);

        userService.delete(user.getId());
        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
    }

    @Test
    @DisplayName("User not found in delete method")
    void deleteNotFound() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User result = userService.findById(1L);
        Assertions.assertEquals(null, result);

        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(userRepository, Mockito.never()).delete(user);
    }
}