package io.philo.domain.service;

import io.philo.domain.entity.User;
import io.philo.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository repository;

  public Long createUser(String email, String name, String address, String password) {
    var user = new User(email, name, address, password);
    repository.save(user);
    return user.getId();
  }
}
