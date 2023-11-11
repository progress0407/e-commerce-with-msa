package io.philo.presentation;

import io.philo.domain.service.UserService;
import io.philo.presentation.dto.UserCreateRequest;
import io.philo.presentation.dto.UserCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
  public UserCreateResponse create(UserCreateRequest request) {

    Long userId = userService.createUser(
        request.email(),
        request.name(),
        request.address(),
        request.password()
    );

    return new UserCreateResponse(userId);
  }
}
