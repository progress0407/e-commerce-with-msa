package io.philo.shop.application

import io.philo.shop.domain.core.OrderEntity
import io.philo.shop.domain.core.OrderLineItemEntity
import io.philo.shop.domain.outbox.OrderCreatedOutboxEntity
import io.philo.shop.domain.outbox.OrderFailedOutboxEntity
import io.philo.shop.entity.OutboxBaseEntity
import io.philo.shop.error.EntityNotFoundException
import io.philo.shop.messagequeue.OrderEventPublisher
import io.philo.shop.order.OrderChangedEvent
import io.philo.shop.order.OrderLineCreatedEvent
import io.philo.shop.repository.OrderCreatedOutboxRepository
import io.philo.shop.repository.OrderFailedOutboxRepository
import io.philo.shop.repository.OrderRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 다른 마이크로 서비스와 상호작용 후 시스템에 반영하기 위한 역할
 */
@Service
class OrderEventService(
    private val orderCreatedOutBoxRepository: OrderCreatedOutboxRepository, // 처음 발송을 한 OutBox
    private val orderFailedOutBoxRepository: OrderFailedOutboxRepository, // 실패한 이벤트에 대해 보상 트랜잭션 밠공을 위한 OutBox
    private val orderRepository: OrderRepository,
    private val orderEventPublisher: OrderEventPublisher,
) {

    private val log = KotlinLogging.logger { }

    @Transactional
    fun processAfterItemVerification(orderId: Long, verification: Boolean) {

        val outBox = orderCreatedOutBoxRepository.findByTraceId(orderId) ?: throw EntityNotFoundException(orderId)
        outBox.changeItemValidated(verification)
    }

    @Transactional
    fun processAfterCouponVerification(orderId: Long, verification: Boolean) {

        val outBox = orderCreatedOutBoxRepository.findByTraceId(orderId) ?: throw EntityNotFoundException(orderId)
        outBox.changeCouponValidated(verification)
    }

    /**
     * 주문 대기(PENDING) 상태를 처리한다
     */
    @Transactional
    fun completeOrderEvent() {

        val outboxes = orderCreatedOutBoxRepository.findAllToCompleteEvent()

        if (outboxes.isNullOrEmpty())
            return

        log.info { "완료할 이벤트가 존재합니다." }

        val outBoxMap = outboxes.associateBy { it.traceId }
        val orderIds = outboxes.map { it.traceId }.toList()
        val orders = orderRepository.findAllByIdIn(orderIds)

        // 이벤트 처리 및 outbox 데이터 제거
        for (order in orders) {
            val outBox = outBoxMap[order.id]!!
            if (outBox.isSuccess)
                order.completeToSuccess()
            else
                order.completeToFail()
        }
        orderCreatedOutBoxRepository.deleteAll(outboxes)

        // 실패한 이벤트 존재시 보상 트랜잭션 발송 준비
        // 유효성 검증을 통과한 마이크로 서비스에 보상 트랜잭션을 보낸다
        val failedOrders = orders.filter { it.isFail }.toList()
        if (failedOrders.isEmpty())
            return
        val compensatingOutBoxes = toFailedOutBoxes(failedOrders, outBoxMap)

        orderFailedOutBoxRepository.saveAll(compensatingOutBoxes)
    }

    /**
     * 주문 생성 이벤트를 브로커에 적재한다
     */
    @Transactional
    fun loadEventToBroker() {

        val orderCreatedOutboxes = orderCreatedOutBoxRepository.findAllByLoadedIsFalse()
        loadEventToBrokerInternal(orderCreatedOutboxes) { event ->
            orderEventPublisher.publishEventToItemServer(event)
            orderEventPublisher.publishEventToCouponServer(event)
        }
    }

    /**
     * 주문 보상 이벤트를 브로커에 적재한다
     */
    @Transactional
    fun loadCompensatingEventToBroker() {

        val orderFailedOutboxes = orderFailedOutBoxRepository.findAllByLoadedIsFalse()
        loadEventToBrokerInternal(orderFailedOutboxes) { event ->
            orderEventPublisher.publishReverseEventToItemServer(event)
            orderEventPublisher.publishReverseEventToCouponServer(event)
        }
    }

    /**
     * 일반 트랜잭션과 보상 트랜잭션의 공통 로직을 처리합니다.
     */
    private fun loadEventToBrokerInternal(orderOutboxEntities: List<OutboxBaseEntity>, publishingEventLambda: (OrderChangedEvent) -> Unit) {

        val outboxes: List<OutboxBaseEntity> = orderOutboxEntities

        if (outboxes.isNullOrEmpty())
            return

        log.info { "브로커에 적재할 이벤트가 존재합니다." }

        val orderIds = outboxes.extractIds()
        val orderIdToRequesterIdMap = outboxes.orderIdToRequesterIdMap()
        val orderEntities = orderRepository.findAllByIdIn(orderIds)
        val events = convertToEvents(orderEntities, orderIdToRequesterIdMap)

        for (event in events) {
            publishingEventLambda.invoke(event)
            changeOutboxStatusToLoad(outboxes, event)
        }
    }

    private fun toFailedOutBoxes(
        orderEntities: List<OrderEntity>,
        outBoxMap: Map<Long, OrderCreatedOutboxEntity>,
    ): List<OrderFailedOutboxEntity> =
        orderEntities
            .map { failedOrder -> toFailedOutBox(failedOrder, outBoxMap) }
            .toList()

    private fun toFailedOutBox(failedOrder: OrderEntity, outBoxMap: Map<Long, OrderCreatedOutboxEntity>): OrderFailedOutboxEntity {

        val outbox = outBoxMap[failedOrder.id]!!
        val orderId = outbox.traceId
        val requesterId = outbox.requesterId

        return OrderFailedOutboxEntity(
            traceId = orderId,
            requesterId = requesterId,
            isCompensatingItem = outbox.itemValidated.toBool,
            isCompensatingOrder = outbox.couponValidated.toBool
        )
    }

    /**
     * todo!
     *
     * kafka의 경우 event에 적재됨을 확인하면, (acks=1 이상) (offset >=0)
     * 이후에 Load 상태로 바꾸게 변경하자
     */
    private fun changeOutboxStatusToLoad(outboxes: List<OutboxBaseEntity>, event: OrderChangedEvent) {

        val matchedOutBox = outboxes.find { it.id == event.orderId }!!
        matchedOutBox.load()
    }

    private fun List<OutboxBaseEntity>.extractIds() =
        this.map { it.traceId }.toList()

    private fun List<OutboxBaseEntity>.orderIdToRequesterIdMap() =
        this.associateBy({ it.traceId }, { it.requesterId })

    private fun convertToEvents(orderEntities: List<OrderEntity>, orderIdToRequesterIdMap: Map<Long, Long>): List<OrderChangedEvent> {
        return orderEntities.map { OrderChangedEvent.from(it, orderIdToRequesterIdMap) }.toList()
    }

    private fun OrderChangedEvent.Companion.from(orderEntity: OrderEntity, orderIdToRequesterIdMap: Map<Long, Long>): OrderChangedEvent {

        val orderLineEntities = orderEntity.orderLineItemEntities
        val orderLineEvents = orderLineEntities.map { OrderLineCreatedEvent.from(it) }.toList()
        val requesterId = orderIdToRequesterIdMap[orderEntity.id!!]!!

        return OrderChangedEvent(
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

        val couponIds = getCouponIds(orderLineEntity)

        return OrderLineCreatedEvent(
            itemId = orderLineEntity.itemId,
            itemAmount = orderLineEntity.itemRawAmount,
            itemDiscountedAmount = orderLineEntity.itemDiscountedAmount,
            itemQuantity = orderLineEntity.orderedQuantity,
            userCouponIds = couponIds
        )
    }
}