package io.philo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
object UserApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.run(UserApplication::class.java, *args)
    }
}
