package com.dongfg.poc.jpa_soft_delete

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JpaSoftDeleteApplicationTests {

    @Autowired
    private lateinit var bankCardService: BankCardService

    @Test
    fun contextLoads() {
        assertNotNull(bankCardService)
    }
}