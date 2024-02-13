package io.philo.shop.application

import io.philo.shop.coupon.CouponRestClientFacade
import io.philo.shop.domain.core.OrderEntity
import io.philo.shop.domain.core.OrderLineItemEntity
import io.philo.shop.domain.outbox.OrderCreatedOutboxEntity
import io.philo.shop.dto.web.OrderLineRequestDto
import io.philo.shop.error.BadRequestException
import io.philo.shop.item.ItemRestClientFacade
import io.philo.shop.messagequeue.OrderEventPublisher
import io.philo.shop.repository.OrderCreatedOutBoxRepository
import io.philo.shop.repository.OrderRepository
import lombok.RequiredArgsConstructor
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderOutBoxRepository: OrderCreatedOutBoxRepository,
    private val itemClient: ItemRestClientFacade,
    private val couponClient: CouponRestClientFacade,
    private val orderEventPublisher: OrderEventPublisher,
) {
    private val log = KotlinLogging.logger { }

    /**
     * 비동기 호출로 만들 것: order 생성 -> PENDING
     *
     * item, coupon, payment 서비스에 validate and decrease 요청
     *
     * out box pattern 으로 요청
     */
    @Transactional
    fun order(orderLineDtos: List<OrderLineRequestDto>, requesterId: Long): Long {

        validateCouponUsable(orderLineDtos)

        val orderEntity = OrderEntity.createOrder(orderLineDtos, requesterId)
        orderRepository.save(orderEntity)

        val outbox = OrderCreatedOutboxEntity(orderEntity.id!!, requesterId)
        orderOutBoxRepository.save(outbox)

        return orderEntity.id!!
    }

    /**
     * 쿠폰 사용 가능 여부 검증
     *
     * 쿠폰은 하나의 상품에 대해서만 사용할 수 있습니다.
     */
    private fun validateCouponUsable(orderLineDtos: List<OrderLineRequestDto>) {

        for (orderLineDto in orderLineDtos) {
            if (orderLineDto.userCouponIds != null && orderLineDto.itemQuantity >= 2) {
                throw BadRequestException("한 쿠폰을 둘 이상의 상품에 동시 적용할 수 없습니다.")
            }
        }
    }

    private fun OrderEntity.Companion.createOrder(orderLineDtos: List<OrderLineRequestDto>, requesterId: Long): OrderEntity {

        val orderItems = orderLineDtos.toEntities()
        return OrderEntity(requesterId, orderItems)
    }

    private fun List<OrderLineRequestDto>.toEntities(): MutableList<OrderLineItemEntity> =
        this
            .map { dto -> createOrderLine(dto) }
            .toMutableList()

    private fun createOrderLine(dto: OrderLineRequestDto): OrderLineItemEntity {

        val orderLineItemEntity = OrderLineItemEntity(
            itemId = dto.itemId,
            itemRawAmount = dto.itemAmount,
            itemDiscountedAmount = dto.itemDiscountedAmount,
            orderedQuantity = dto.itemQuantity,
        )

        orderLineItemEntity.initUserCoupon(dto.userCouponIds)

        return orderLineItemEntity
    }
}