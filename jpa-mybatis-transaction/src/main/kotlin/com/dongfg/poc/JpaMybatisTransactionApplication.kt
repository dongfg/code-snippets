package com.dongfg.poc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JpaMybatisTransactionApplication

fun main(args: Array<String>) {
    runApplication<JpaMybatisTransactionApplication>(*args)
}
