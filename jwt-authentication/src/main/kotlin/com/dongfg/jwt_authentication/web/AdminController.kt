package com.dongfg.jwt_authentication.web

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/admin")
@RestController
class AdminController {
    @GetMapping
    fun get() = "You got it! admin user"

    @GetMapping("/user")
    fun user(@AuthenticationPrincipal(expression = "user") adminUser: AdminUser): AdminUser = adminUser
}