package msa.with.ddd.ui

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RefreshScope
class ApiGatewayController(@Value("\${constant.test.value}") private val testConstantValue: String) {

    @GetMapping("/gw/env-test")
    fun testConstantValue() = "check value for dynamical refresh test: $testConstantValue"
}