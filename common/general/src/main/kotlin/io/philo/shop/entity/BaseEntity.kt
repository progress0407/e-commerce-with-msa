package io.philo.shop.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import java.time.LocalDateTime.now

@MappedSuperclass
abstract class BaseEntity {

    @field:CreatedDate
    @field:Column(nullable = false)
    val createdAt: LocalDateTime = now()

    @field:LastModifiedDate
    @field:Column(nullable = false)
    var updatedAt: LocalDateTime = now()
}