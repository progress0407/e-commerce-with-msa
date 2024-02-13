package io.philo.shop.domain.replica

import org.springframework.data.jpa.repository.JpaRepository

interface ItemReplicaRepository : JpaRepository<ItemReplicaEntity, Long> {
}