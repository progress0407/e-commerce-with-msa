package io.philo.shop.scheduler

import io.philo.shop.domain.core.OrderEntity
import io.philo.shop.domain.core.OrderLineItemEntity
import io.philo.shop.domain.outbox.OrderCreatedOutboxEntity
import io.philo.shop.messagequeue.OrderEventPublisher
import io.philo.shop.order.OrderCreatedEvent
import io.philo.shop.order.OrderLineCreatedEvent
import io.philo.shop.repository.OrderCreatedOutBoxRepository
import io.philo.shop.repository.OrderRepository
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * OutBox에 저장한 이벤트를 브로커에 적재하는 역할
 */
@Component
class OrderEventLoader(
    private val outBoxRepository: OrderCreatedOutBoxRepository,
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
        val orderIdToRequesterIdMap = outboxes.associateBy({ it.traceId }, { it.requesterId })
        val orderEntities = orderRepository.findAllByIdIn(orderIds)
        val events = convertToEvents(orderEntities, orderIdToRequesterIdMap)

        for (event in events) {
            orderEventPublisher.publishEventToItemServer(event)
            orderEventPublisher.publishEventToCouponServer(event)

            // todo!
            // kafka의 경우 event에 적재됨을 확인하면, (acks=1 이상) (offset >=0)
            // 이후에 Load 상태로 바꾸게 변경하자
            changeOutBoxStatusToLoad(outboxes, event)
        }
    }

    private fun changeOutBoxStatusToLoad(outboxes: List<OrderCreatedOutboxEntity>, event: OrderCreatedEvent) {

        val matchedOutBox = outboxes.find { it.id == event.orderId }!!
        matchedOutBox.load()
        outBoxRepository.save(matchedOutBox)
    }

    private fun List<OrderCreatedOutboxEntity>.extractIds() =
        this.map { it.traceId }.toList()

    private fun convertToEvents(orderEntities: List<OrderEntity>, orderIdToRequesterIdMap: Map<Long, Long>): List<OrderCreatedEvent> {
        return orderEntities.map { OrderCreatedEvent.from(it, orderIdToRequesterIdMap) }.toList()
    }

    private fun OrderCreatedEvent.Companion.from(orderEntity: OrderEntity, orderIdToRequesterIdMap: Map<Long, Long>): OrderCreatedEvent {

        val orderLineEntities = orderEntity.orderLineItemEntities
        val orderLineEvents = orderLineEntities.map { OrderLineCreatedEvent.from(it) }.toList()
        val requesterId = orderIdToRequesterIdMap[orderEntity.id!!]!!

        return OrderCreatedEvent(
            orderId = orderEntity.id!!,
            requesterId = requesterId,
            orderLineCreatedEvents = orderLineEvents
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
}
