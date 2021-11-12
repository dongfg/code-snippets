package com.dongfg.multiple_authentication

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/public")
@RestController
class PublicApiController {

    @GetMapping
    fun get(@AuthenticationPrincipal user: UserDetails): String {
        println("Public Api Access By: ${user.username}")
        return "I am a public get api"
    }
}

@RequestMapping("/api")
@RestController
class ApiController {

    @GetMapping
    fun get(@AuthenticationPrincipal user: UserDetails): String {
        println("Api Access By: ${user.username}")
        return "I am a get api"
    }
}

@RequestMapping("/admin")
@RestController
class AdminApiController {

    @GetMapping
    fun get(@AuthenticationPrincipal user: UserDetails): String {
        println("Admin Api Access By: ${user.username}")
        return "I am an admin get api"
    }
}

@RequestMapping("/")
@RestController
class IndexController {

    @GetMapping
    fun get() = "It works!"
}