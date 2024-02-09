package io.philo.shop

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

/**
 * Facade Of Password Encoding
 */
object PasswordEncoder {
    private val encoder = BCryptPasswordEncoder()
    fun encodePassword(rawPassword: String?): String {
        return encoder.encode(rawPassword)
    }

    fun isSamePassword(rawPassword: String?, encodedPassword: String?): Boolean {
        return encoder.matches(rawPassword, encodedPassword)
    }
}
