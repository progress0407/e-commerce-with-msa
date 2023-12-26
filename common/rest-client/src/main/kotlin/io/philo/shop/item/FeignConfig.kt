package io.philo.shop.item

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignConfig {

    @Bean
    fun itemRestClientFacade(itemHttpClient: ItemFeignClient): ItemRestClientFacade {
        return ItemRestClientFacade(itemHttpClient)
    }
}