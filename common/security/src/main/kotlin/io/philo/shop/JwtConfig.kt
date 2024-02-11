package io.philo.shop

import io.jsonwebtoken.security.Keys
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.charset.StandardCharsets

@Configuration
class JwtConfig {

    // todo! 1. yaml 분리 후  2. repository 외부로 값을 추출할 것
    private val SECRET_KEY_STRING = "abc 123 abc 123 abc 123 abc 123 abc 123 abc 123 abc 123 abc 123 abc 123 abc 123"
    private val secretKey = Keys.hmacShaKeyFor(SECRET_KEY_STRING.toByteArray(StandardCharsets.UTF_8))
    private val expirationDurationTime: Long = 60 * 60 * 1000

    @Bean
    fun jwtManager() = JwtManager(secretKey, expirationDurationTime)
}