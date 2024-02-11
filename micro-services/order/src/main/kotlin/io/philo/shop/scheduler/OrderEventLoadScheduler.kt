package io.philo.shop.scheduler

import io.philo.shop.domain.OrderEntity
import io.philo.shop.domain.OrderLineItemEntity
import io.philo.shop.domain.OrderOutBox
import io.philo.shop.message.OrderEventPublisher
import io.philo.shop.order.OrderCreatedEvent
import io.philo.shop.order.OrderLineCreatedEvent
import io.philo.shop.repository.OrderOutBoxRepository
import io.philo.shop.repository.OrderRepository
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * OutBox에 저장한 이벤트를 브로커에 적재하는 역할
 */
@Component
class OrderEventLoadScheduler(
    private val outBoxRepository: OrderOutBoxRepository,
    private val orderRepository: OrderRepository,
    private val orderEventPublisher: OrderEventPublisher,
) {

    private val log = KotlinLogging.logger { }

    @Scheduled(fixedDelay = 1_000)
    fun loadEventToBroker() {

        val outboxes = outBoxRepository.findAllByLoadedIsFalse()

        if (outboxes.isNullOrEmpty())
            return

        log.info { "브로커에 적재할 이벤트가 존재합니다." }

        val orderIds = outboxes.extractIds()
        val orderEntities: List<OrderEntity> = orderRepository.findAllByIdIn(orderIds)
        val events = orderEntities.convertToEvents()

        for (event in events) {
            orderEventPublisher.publishEvent(event)
            // todo!
            // kafka의 경우 event에 적재됨을 확인하면, (acks=1 이상)
            // 이후에 Load 상태로 바꾸게 변경하자
            changeOutBoxStatusToLoad(outboxes, event)
        }
    }

    private fun changeOutBoxStatusToLoad(outboxes: List<OrderOutBox>, event: OrderCreatedEvent) {

        val matchedOutBox = outboxes.find { it.id == event.orderId }!!
        matchedOutBox.load()
        outBoxRepository.save(matchedOutBox)
    }

    private fun List<OrderOutBox>.extractIds() =
        this.map { it.orderId }.toList()

    private fun List<OrderEntity>.convertToEvents(): List<OrderCreatedEvent> =
        this.map { OrderCreatedEvent.from(it) }.toList()

    private fun OrderCreatedEvent.Companion.from(orderEntity: OrderEntity): OrderCreatedEvent {

        val orderLineEntities = orderEntity.orderLineItemEntities
        val orderLineEvents = orderLineEntities.map { OrderLineCreatedEvent.from(it) }.toList()

        return OrderCreatedEvent(
            orderId = orderEntity.id!!,
            orderLineCreatedEvents = orderLineEvents
        )
    }

    private fun OrderLineCreatedEvent.Companion.from(orderLineEntity: OrderLineItemEntity): OrderLineCreatedEvent {


        val couponIds: List<Long>? = getCouponIds(orderLineEntity)

        return OrderLineCreatedEvent(
            itemId = orderLineEntity.itemId,
            itemAmount = orderLineEntity.itemRawAmount,
            itemDiscountedAmount = orderLineEntity.itemDiscountedAmount,
            itemQuantity = orderLineEntity.orderedQuantity,
            userCouponIds = couponIds
        )
    }

    private fun getCouponIds(orderLineEntity: OrderLineItemEntity): List<Long>? {

        return if (orderLineEntity.useCoupon) {
            val coupons = orderLineEntity.coupons!!
            listOfNotNull(coupons.userCouponId1, coupons.userCouponId2)
        } else {
            return null
        }
    }
}
