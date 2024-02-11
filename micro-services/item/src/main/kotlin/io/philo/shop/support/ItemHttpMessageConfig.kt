package io.philo.shop.support

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration

@Configuration
class ItemHttpMessageConfig {

    @Autowired
    lateinit var applicationContext: ApplicationContext

    @PostConstruct
    fun init() {
        val bean = applicationContext.getBean(HttpMessageConverters::class.java)
        println("bean = ${bean}")
    }
}