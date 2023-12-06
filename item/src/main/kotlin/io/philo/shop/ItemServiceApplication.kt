package io.philo.shop

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class ItemServiceApplication

fun main(args: Array<String>) {
    SpringApplication.run(ItemServiceApplication::class.java, *args)
}