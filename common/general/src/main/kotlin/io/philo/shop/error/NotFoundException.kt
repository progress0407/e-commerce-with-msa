package io.philo.shop.error

import org.springframework.http.HttpStatus

open class NotFoundException(message: String?, cause: Throwable?) : RuntimeException(message, cause) {

    private val httpStatus = HttpStatus.NOT_FOUND

    constructor(message: String): this(message, null)
}