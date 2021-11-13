package com.dongfg.jwt_authentication.web

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RequestMapping("/api")
@RestController
class ApiController {
    @GetMapping
    fun get() = "You got it! api user"

    @GetMapping("/user")
    fun user(@AuthenticationPrincipal(expression = "user") apiUser: ApiUser): ApiUser = apiUser
}



