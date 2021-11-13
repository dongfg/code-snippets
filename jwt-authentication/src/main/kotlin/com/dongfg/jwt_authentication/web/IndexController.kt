package com.dongfg.jwt_authentication.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController {
    @GetMapping
    fun get() = "It works!"

    @PostMapping
    fun post() = "It works!"
}