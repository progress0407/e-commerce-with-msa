package io.philo.shop.support

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

/**
 * API Gateway
 */
@Configuration
class GwHttpMessageConfig {

    @Autowired
    lateinit var applicationContext: ApplicationContext

//    @PostConstruct
//    fun init() {
//        val bean = applicationContext.getBean(HttpMessageConverters::class.java)
//        println("bean = ${bean}")
//        println("")
//    }

    @Bean
    fun httpMessageConverters() : HttpMessageConverters {

        return HttpMessageConverters(
            MappingJackson2HttpMessageConverter(),
            StringHttpMessageConverter()
        )
    }
}