package msa.with.ddd

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GatewayConfig {

    @Bean
    fun loggingFilter() = LoggingFilter()
}
