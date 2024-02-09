package io.philo.shop.error

import org.springframework.http.HttpStatus.UNAUTHORIZED

/**
 * 401 예외
 */
open class UnauthorizedException(message: String, cause: Throwable? = null) :
    InAppException(UNAUTHORIZED, message, cause)