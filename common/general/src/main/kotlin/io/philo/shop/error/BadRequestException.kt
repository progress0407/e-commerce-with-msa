package io.philo.shop.error

import org.springframework.http.HttpStatus.BAD_REQUEST

/**
 * 400 예외
 */
open class BadRequestException(message: String, cause: Throwable? = null) :
    InAppException(BAD_REQUEST, message, cause)