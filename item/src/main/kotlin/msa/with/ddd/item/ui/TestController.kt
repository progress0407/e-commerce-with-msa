package msa.with.ddd.item.ui

import org.springframework.web.bind.annotation.*

@RequestMapping("/foo")
@RestController
class TestController {

    @GetMapping
    fun foo() = "hello"
}