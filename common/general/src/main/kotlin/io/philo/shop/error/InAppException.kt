package io.philo.shop.error

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR

/**
 * 현재 프로젝트에서 사용하는 비즈니스 최상위 예외 클래스
 */
open class InAppException(val httpStatus: HttpStatus = INTERNAL_SERVER_ERROR,
                          message: String = "",
                          cause: Throwable? = null) :
    RuntimeException(message, cause) {
}