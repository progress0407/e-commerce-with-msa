package io.philo.shop.presentation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/foo")
@RestController
class TestController {

    @GetMapping
    fun foo() = "hello"
}