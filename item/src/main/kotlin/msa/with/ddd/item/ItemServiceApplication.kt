package msa.with.ddd.item

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ItemServiceApplication

fun main(args: Array<String>) {
    SpringApplication.run(ItemServiceApplication::class.java, *args)
}