package io.philo.domain.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import io.philo.domain.entity.User
import io.philo.domain.repository.UserRepository
import io.philo.shop.error.EntityNotFoundException
import io.philo.shop.error.UnauthorizedException
import io.philo.support.JwtManager
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val jwtManager: JwtManager,
                  private val repository: UserRepository) {


    fun createUser(
        email: String,
        name: String,
        address: String,
        password: String
    ): Long {

        val user = User(email, name, address, password)

        repository.save(user)

        return user.id!!
    }

    fun login(inputEmail: String, inputPassword: String): String {

        val user = repository.findByEmail(inputEmail) ?: throw EntityNotFoundException(inputEmail)

        validateCredential(inputPassword, user)

        val subject = user.id.toString()
        val newAccessToken = jwtManager.createAccessToken(subject)

        return newAccessToken
    }

    private fun validateCredential(inputPassword: String, user: User) {
        if (isCorrectCredential(inputPassword, user).not()) {
            throw UnauthorizedException("유효한 로그인 정보가 아닙니다.")
        }
    }

    private fun isCorrectCredential(inputPassword: String, user: User): Boolean {
        return user.isSamePassword(inputPassword)
    }
}
