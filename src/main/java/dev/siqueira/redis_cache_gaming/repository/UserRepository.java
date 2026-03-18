package dev.siqueira.redis_cache_gaming.repository;

import dev.siqueira.redis_cache_gaming.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
