package io.philo.shop.messagequeue.consumer

import io.philo.shop.domain.entity.item.ItemReplicaEntity
import io.philo.shop.domain.repository.ItemReplicaRepository
import io.philo.shop.item.ItemCreatedEvent
import io.philo.shop.item.ItemRabbitProperty.Companion.ITEM_REPLICA_FOR_COUPON_QUEUE_NAME
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class CouponEventListener(private val itemReplicaRepository: ItemReplicaRepository) {

    /**
     * 상품의 복제본 동기화
     */
    @RabbitListener(queues = [ITEM_REPLICA_FOR_COUPON_QUEUE_NAME])
    fun listenItemReplicaForCoupon(event: ItemCreatedEvent) {

        val entity = event.toEntity()
        itemReplicaRepository.save(entity)
    }
}

private fun ItemCreatedEvent.toEntity() = ItemReplicaEntity(this.id, this.amount)

