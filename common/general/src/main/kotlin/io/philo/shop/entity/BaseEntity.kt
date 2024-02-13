package io.philo.shop.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import java.time.LocalDateTime.now

@MappedSuperclass
abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @field:CreatedDate
    @field:Column(nullable = false)
    val createdAt: LocalDateTime = now()

    @field:LastModifiedDate
    @field:Column(nullable = false)
    var updatedAt: LocalDateTime = now()
}