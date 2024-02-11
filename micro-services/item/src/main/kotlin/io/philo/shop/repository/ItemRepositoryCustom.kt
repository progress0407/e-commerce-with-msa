package io.philo.shop.repository

import io.philo.shop.domain.entity.ItemEntity

@Deprecated("순환 의존성으로 인해 적용 보류")
interface ItemRepositoryCustom {

    fun saveAndPublish(entity: ItemEntity)
}