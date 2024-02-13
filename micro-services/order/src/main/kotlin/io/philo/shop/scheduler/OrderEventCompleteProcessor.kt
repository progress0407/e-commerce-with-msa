package io.philo.shop.scheduler

import io.philo.shop.application.OrderEventService
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * 협력 마이크로 서비스가 검증 결과들을 모두 수신 후 이벤트 완료처리하는 역할
 */
@Component
class OrderEventCompleteProcessor(private val orderEventService: OrderEventService) {

    private val log = KotlinLogging.logger { }

    @Scheduled(fixedDelay = 1_000)
    fun completeOrderEvent() {

        orderEventService.completeOrderEvent()
    }
}