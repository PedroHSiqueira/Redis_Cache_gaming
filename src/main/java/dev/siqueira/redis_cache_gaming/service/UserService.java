package dev.siqueira.redis_cache_gaming.service;

import dev.siqueira.redis_cache_gaming.dtos.UserRequestDto;
import dev.siqueira.redis_cache_gaming.entity.User;
import dev.siqueira.redis_cache_gaming.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository  userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

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
}
