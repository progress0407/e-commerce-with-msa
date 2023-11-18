package io.philo.domain.service;

import io.philo.domain.entity.User;
import io.philo.domain.repository.UserRepository;
import io.philo.shop.error.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository repository;

  public long createUser(String email, String name, String address, String password) {
    var user = new User(email, name, address, password);
    repository.save(user);
    return user.getId();
  }

  public void login(String inputEmail, String inputPassword) {
    User user = repository.findByEmailOrThrow(inputEmail);
    validateCredential(inputEmail, user);
  }

  private static void validateCredential(String inputEmail, User user) {
    if (!isRightCredential(inputEmail, user)) {
      throw new UnauthorizedException("유효한 로그인 정보가 아닙니다.");
    }
  }

  private static boolean isRightCredential(String inputEmail, User user) {
    if (user.getEmail().equals(inputEmail)) {
      if (user.getEncodedPassword().equals(inputEmail)) {
        return true;
      }
      return false;
    }
    return false;
  }
}
