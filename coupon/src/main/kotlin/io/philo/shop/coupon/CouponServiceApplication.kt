package io.philo.shop.coupon

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class CouponServiceApplication

fun main(args: Array<String>) {
    SpringApplication.run(CouponServiceApplication::class.java, *args)
}