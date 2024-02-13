package io.philo.shop.application

import io.philo.shop.domain.core.OrderEntity
import io.philo.shop.domain.outbox.OrderCreatedOutBoxEntity
import io.philo.shop.domain.outbox.OrderFailedOutBoxEntity
import io.philo.shop.error.EntityNotFoundException
import io.philo.shop.repository.OrderCreatedOutBoxRepository
import io.philo.shop.repository.OrderFailedOutBoxRepository
import io.philo.shop.repository.OrderRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 다른 마이크로 서비스와 상호작용 후 시스템에 반영하기 위한 역할
 */
@Service
class OrderEventService(
    private val firstEventOutBoxRepository: OrderCreatedOutBoxRepository, // 처음 발송을 한 OutBox
    private val orderFailedOutBoxRepository: OrderFailedOutBoxRepository, // 실패한 이벤트에 대해 보상 트랜잭션 밠공을 위한 OutBox
    private val orderRepository: OrderRepository,
) {

    private val log = KotlinLogging.logger { }

    @Transactional
    fun processAfterItemVerification(orderId: Long, verification: Boolean) {

        val outBox = firstEventOutBoxRepository.findByOrderId(orderId) ?: throw EntityNotFoundException(orderId)
        outBox.changeItemValidated(verification)
    }

    @Transactional
    fun processAfterCouponVerification(orderId: Long, verification: Boolean) {

        val outBox = firstEventOutBoxRepository.findByOrderId(orderId) ?: throw EntityNotFoundException(orderId)
        outBox.changeCouponValidated(verification)
    }

    /**
     * 주문 대기(PENDING) 상태를 처리한다
     */
    @Transactional
    fun completeOrderEvent() {

        val outboxes = firstEventOutBoxRepository.findAllToCompleteEvent()

        if (outboxes.isNullOrEmpty())
            return

        log.info { "완료할 이벤트가 존재합니다." }

        val outBoxMap = outboxes.associateBy { it.orderId }
        val orderIds = outboxes.map { it.orderId }.toList()
        val orders = orderRepository.findAllByIdIn(orderIds)

        // 이벤트 처리
        for (order in orders) {
            val outBox = outBoxMap[order.id]!!
            if (outBox.isSuccess) {
                order.completeToSuccess()
            } else {
                order.completeToFail()
            }
        }

        // 실패한 이벤트 존재시 보상 트랜잭션 발송 준비
        // 유효성 검증을 통과한 마이크로 서비스에 보상 트랜잭션을 보낸다
        val failedOrders = orders.filter { it.isFail }.toList()
        if (failedOrders.isEmpty())
            return
        val compensatingOutBoxes = toFailedOutBoxes(failedOrders, outBoxMap)

        orderFailedOutBoxRepository.saveAll(compensatingOutBoxes)
    }

    private fun toFailedOutBoxes(
        orderEntities: List<OrderEntity>,
        outBoxMap: Map<Long, OrderCreatedOutBoxEntity>
    ): List<OrderFailedOutBoxEntity> =
        orderEntities
            .map { failedOrder -> toFailedOutBox(failedOrder, outBoxMap) }
            .toList()

    private fun toFailedOutBox(failedOrder: OrderEntity, outBoxMap: Map<Long, OrderCreatedOutBoxEntity>): OrderFailedOutBoxEntity {

        val outbox = outBoxMap[failedOrder.id]!!
        val orderId = outbox.orderId
        val requesterId = outbox.requesterId

        return OrderFailedOutBoxEntity(
            orderId = orderId,
            requesterId = requesterId,
            isCompensatingItem = outbox.itemValidated.toBool,
            isCompensatingOrder = outbox.couponValidated.toBool
        )
    }

}