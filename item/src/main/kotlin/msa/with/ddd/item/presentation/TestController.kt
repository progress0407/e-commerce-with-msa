package msa.with.ddd.item.presentation

import org.springframework.web.bind.annotation.*

@RequestMapping("/foo")
@RestController
class TestController {

    @GetMapping
    fun foo() = "hello"
}