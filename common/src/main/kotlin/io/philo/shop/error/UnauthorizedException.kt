package io.philo.shop.error

import org.springframework.http.HttpStatus

open class UnauthorizedException(message: String?, cause: Throwable?) : RuntimeException(message, cause) {

    private val httpStatus = HttpStatus.UNAUTHORIZED

    constructor(message: String): this(message, null)
}