package com.dongfg.rbac

import com.github.javafaker.Faker
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@DataJpaTest
class JpaRepositoryTests {
    @Autowired
    private lateinit var userRepository: UserRepository

    private val faker = Faker(Locale.CHINA)
    private val defaultPassword = BCryptPasswordEncoder().encode("123456")

    @Test
    fun user() {
        val user1 = User(
            username = faker.name().username(),
            password = defaultPassword,
            roleId = -1
        )
        userRepository.save(user1).also { println(user1) }
        assertNotNull(user1.id)

        val user2 = User(
            username = faker.name().username(),
            password = defaultPassword,
            roleId = -1
        )
        userRepository.save(user2).also { println(user2) }
        assertNotNull(user2.id)

        assertEquals(1, user2.id!! - user1.id!!)
    }
}