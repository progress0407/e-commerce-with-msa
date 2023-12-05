package io.philo.shop.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity(

    @field:CreatedDate
    @field:Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @field:LastModifiedDate
    @field:Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
}