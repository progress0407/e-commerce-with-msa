package io.philo.shop.presentation

import com.netflix.discovery.EurekaClient
import com.netflix.discovery.shared.Application
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api-gateway")
@RefreshScope
class ApiGatewayController(
    @Value("\${constant.test.value}")
    private val testConstantValue: String,
    private val routeLocator: RouteLocator,
    private val discoveryClient: DiscoveryClient,
    private val eurekaClient: EurekaClient
) {

    @GetMapping("/env-test")
    fun testConstantValue(): String {
        return "check value for dynamical refresh test: $testConstantValue"
    }

    @GetMapping("/route-info")
    fun routeLocator(): RouteLocator {
        return routeLocator
    }

    @GetMapping("/routes/service-instances")
    fun instances3(): Map<String, MutableList<ServiceInstance>> {
        return discoveryClient.services.associateWith { serviceId ->
            discoveryClient.getInstances(serviceId)
        }
    }

    @GetMapping("/routes/applications")
    fun instances4(): Map<String, Application> {
        return discoveryClient.services.associateWith { serviceId ->
            eurekaClient.getApplication(serviceId)
        }
    }
}
