package io.philo.shop.common

import org.springframework.stereotype.Component

/**
 * 현재 애플리케이션에서 사용하는 이벤트 리스너
 *
 * 마커 어노테이션 - 소스 코드 추적용으로 사용
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Component
annotation class InAppEventListener()
