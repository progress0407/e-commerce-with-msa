package io.philo.shop.eureka

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@SpringBootApplication
@EnableEurekaServer
class EurekaApp

fun main(args: Array<String>) {
    SpringApplication.run(EurekaApp::class.java, *args)
}
