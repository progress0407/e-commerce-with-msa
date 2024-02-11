package io.philo.shop.domain.repository

import io.philo.shop.domain.entity.item.ItemReplicaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ItemReplicaRepository : JpaRepository<ItemReplicaEntity, Long> {
}