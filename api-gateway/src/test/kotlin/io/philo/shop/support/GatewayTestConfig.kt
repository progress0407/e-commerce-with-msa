package io.philo.shop.support

import org.springframework.http.codec.ServerCodecConfigurer

//@TestConfiguration
class GatewayTestConfig {
//    @Bean
    fun serverCodecConfigurer(): ServerCodecConfigurer {
        return ServerCodecConfigurer.create()
    }
}