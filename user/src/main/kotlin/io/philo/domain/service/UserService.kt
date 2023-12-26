package io.philo.domain.service

import io.philo.domain.entity.User
import io.philo.domain.repository.UserRepository
import io.philo.shop.error.EntityNotFoundException
import io.philo.shop.error.UnauthorizedException
import io.philo.shop.user.dto.UserPassportResponse
import io.philo.support.JwtManager
import org.springframework.stereotype.Service

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

        // todo 더 암호화하기... 이렇게 되면 보안에 취약하다
        val subject = user.id.toString()
        val newAccessToken = jwtManager.createAccessToken(subject)

        return newAccessToken
    }

    fun isValidToken(accessToken: String): Boolean {
        return jwtManager.isValidToken(accessToken)
    }

    fun passport(accessToken: String): UserPassportResponse {

        return if (jwtManager.isValidToken(accessToken)) {
            val userInfo = jwtManager.parse(accessToken)
            val user: User? = repository.findById(userInfo.toLong()).orElseGet(null)
            if (user == null) {
                UserPassportResponse.OfInvalid()
            } else {
                UserPassportResponse.OfValid(user.id!!, user.name, user.email)
            }
        } else {
            UserPassportResponse.OfInvalid()
        }
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
