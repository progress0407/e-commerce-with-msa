package io.philo.shop.item

//@Configuration
class FeignConfig {

//    @Bean
    fun itemRestClientFacade(itemHttpClient: ItemFeignClient): ItemRestClientFacade {
        return ItemRestClientFacade(itemHttpClient)
    }
}