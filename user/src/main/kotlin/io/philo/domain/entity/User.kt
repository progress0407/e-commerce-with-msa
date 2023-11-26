package io.philo.domain.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import io.philo.support.PasswordEncoder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class User {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column
  private Long id;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  private String encodedPassword;

  public User(String email, String name, String address, String rawPassword) {
    this.email = email;
    this.name = name;
    this.address = address;
    this.encodedPassword = PasswordEncoder.encodePassword(rawPassword);
  }

  public boolean isSamePassword(String rawPassword) {
    return PasswordEncoder.isSamePassword(rawPassword, encodedPassword);
  }
}
