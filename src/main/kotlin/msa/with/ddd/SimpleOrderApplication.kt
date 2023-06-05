package msa.with.ddd

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class SimpleOrderApplication

fun main(args: Array<String>) {

    SpringApplication.run(SimpleOrderApplication::class.java, *args)
}