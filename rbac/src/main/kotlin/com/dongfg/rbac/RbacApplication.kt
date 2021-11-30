package com.dongfg.rbac

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RbacApplication

fun main(args: Array<String>) {
    runApplication<RbacApplication>(*args)
}
