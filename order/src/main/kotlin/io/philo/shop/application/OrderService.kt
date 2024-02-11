package io.philo.shop.application

import io.philo.shop.coupon.CouponRestClientFacade
import io.philo.shop.domain.OrderEntity
import io.philo.shop.domain.OrderLineItemEntity
import io.philo.shop.domain.OrderOutBox
import io.philo.shop.dto.web.OrderLineRequestDto
import io.philo.shop.error.BadRequestException
import io.philo.shop.item.ItemRestClientFacade
import io.philo.shop.message.OrderEventPublisher
import io.philo.shop.repository.OrderOutBoxRepository
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
    private val orderOutBoxRepository: OrderOutBoxRepository,
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
    fun order(orderLineDtos: List<OrderLineRequestDto>): Long {

        validateCouponUsable(orderLineDtos)

        val orderEntity = OrderEntity.createOrder(orderLineDtos)
        orderRepository.save(orderEntity)

        val outbox = OrderOutBox(orderEntity.id!!)
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
            if (orderLineDto.userCouponIds != null && orderLineDto.itemQuantity >= 1) {
                throw BadRequestException("한 쿠폰을 둘 이상의 상품에 동시 적용할 수 없습니다.")
            }
        }
    }

    private fun OrderEntity.Companion.createOrder(orderLineDtos: List<OrderLineRequestDto>): OrderEntity {

        val orderItems = orderLineDtos.toEntities()
        return OrderEntity(orderItems)
    }

    private fun List<OrderLineRequestDto>.toEntities(): MutableList<OrderLineItemEntity> =
        this
            .map { dto -> createOrderLine(dto) }
            .toMutableList()

    private fun createOrderLine(dto: OrderLineRequestDto): OrderLineItemEntity {

        val orderLineItemEntity = OrderLineItemEntity(
            itemId = dto.itemId,
            itemRawAmount = dto.itemAmount,
            itemDiscountedAmount = dto.itemAmount,
            orderedQuantity = dto.itemQuantity,
        )

        orderLineItemEntity.initUserCoupon(dto.userCouponIds)

        return orderLineItemEntity
    }

    /*
        @Transactional
        fun order(orderLineRequests: List<OrderLineRequest>): Long {

            validateCouponUseable(orderLineRequests)

            val itemIds = extractItemIds(orderLineRequests)
            val itemResponses = itemClient.requestItems(itemIds)
            val discountAmountMap = couponClient.requestItemCostsByIds(itemIds)

            val orderItems = createOrderLines(itemResponses, orderLineRequests,discountAmountMap)
            val order = Order.createOrder(orderItems)
            orderRepository.save(order)

           /**
             * 결제가 성공한다면 아래 로직 수행?
             *
             * DB 커넥션을 아끼고 싶다면,
             *
             * 비동기로 할 것.
             */
            orderEventPublisher.publishEvent(order)

            return order.id!!
        }
    */
    /*
        private fun extractItemIds(orderLineRequestDtos: List<OrderLineRequestDto>): List<Long> {
            return orderLineRequestDtos
                .map(OrderLineRequestDto::itemId)
                .toList()
        }
    */

    /*
        private fun createOrderLines(
            itemResponses: List<ItemInternalResponseDto>,
            orderLineRequestDtos: List<OrderLineRequestDto>,
            discountAmountMap: Map<Long, Int>,
        ): MutableList<OrderLineItemEntity> =
            orderLineRequestDtos
                .map { request: OrderLineRequestDto -> createOrderLine(itemResponses, request, discountAmountMap) }
                .toMutableList()
    */

    /*
        private fun findItemDtoFromOrderLineRequest(
            itemResponses: List<ItemInternalResponseDto>,
            orderLineRequestDto: OrderLineRequestDto,
        ): ItemInternalResponseDto {
            return itemResponses.stream()
                .filter { it: ItemInternalResponseDto -> it.id == orderLineRequestDto.itemId }
                .findAny()
                .orElseThrow { IllegalArgumentException("주문 항목 요청에 해당하는 상품이 없습니다.") }
        }
    */

    /*
        private fun createOrderLine(
            itemResponses: List<ItemInternalResponseDto>,
            orderLineRequestDto: OrderLineRequestDto,
            discountAmountMap: Map<Long, Int>,
        ): OrderLineItemEntity {
            val itemResponse = findItemDtoFromOrderLineRequest(itemResponses, orderLineRequestDto)
            val itemId = itemResponse.id
            return OrderLineItemEntity(
                itemId = itemId,
                itemName = itemResponse.name,
                size = itemResponse.size,
                orderItemPrice = discountAmountMap[itemId]!!,
                orderedQuantity = orderLineRequestDto.quantity
            )
        }
    */
}