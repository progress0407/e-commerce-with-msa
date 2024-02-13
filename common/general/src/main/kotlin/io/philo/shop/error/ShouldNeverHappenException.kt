package io.philo.shop.error

import org.springframework.http.HttpStatus

/**
 * 시스템 설계상 일어날 수 없는 예외 발생
 *
 * 코틀린 문법으로 보완할 수 없는 상황에서 사용하기 위한 예외 클래스
 */
class ShouldNeverHappenException(cause: Throwable? = null) :
    InAppException(HttpStatus.INTERNAL_SERVER_ERROR, "존재할 수 없는 예외입니다.", cause)