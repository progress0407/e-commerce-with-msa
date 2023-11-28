package io.philo.config

import io.philo.support.JwtManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SecurityConfig {

    @Bean
    fun jwtManager() = JwtManager()
}