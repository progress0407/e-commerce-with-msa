package io.philo.support;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public abstract class PasswordEncoder {

  private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  public static String encodePassword(String rawPassword) {
    return encoder.encode(rawPassword);
  }

  public static boolean isSamePassword(String rawPassword, String encodedPassword) {
    return encoder.matches(rawPassword, encodedPassword);
  }
}
