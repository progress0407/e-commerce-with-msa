package io.philo.domain.repository;

import io.philo.domain.entity.User;
import io.philo.shop.error.EntityNotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  default User findByEmailOrThrow(String email) {
    return findByEmail(email).orElseThrow(() -> new EntityNotFoundException(email));
  }
}
