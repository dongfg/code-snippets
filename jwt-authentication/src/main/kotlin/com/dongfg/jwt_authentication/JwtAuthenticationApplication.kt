package com.dongfg.jwt_authentication

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JwtAuthenticationApplication

fun main(args: Array<String>) {
    runApplication<JwtAuthenticationApplication>(*args)
}
