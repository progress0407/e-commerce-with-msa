package msa.with.ddd.item.ui

import msa.with.ddd.item.application.ItemService
import msa.with.ddd.item.web.ItemCreateRequest
import msa.with.ddd.item.web.ItemResponses
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("/foo")
@RestController
class FooController {

    @GetMapping
    fun foo() = "hello"
}