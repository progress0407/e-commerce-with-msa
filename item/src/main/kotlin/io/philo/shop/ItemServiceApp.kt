package io.philo.shop

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class ItemServiceApp

fun main(args: Array<String>) {
    runApplication<ItemServiceApp>(*args)
}