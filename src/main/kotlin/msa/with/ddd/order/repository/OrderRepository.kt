package msa.with.ddd.order.repository

import msa.with.ddd.order.domain.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long>