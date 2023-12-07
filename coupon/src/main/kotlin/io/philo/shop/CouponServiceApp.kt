package io.philo.shop

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class CouponServiceApp

fun main(args: Array<String>) {
    SpringApplication.run(CouponServiceApp::class.java, *args)
}