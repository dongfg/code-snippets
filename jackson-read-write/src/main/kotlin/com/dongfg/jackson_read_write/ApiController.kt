package com.dongfg.jackson_read_write

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class ApiController {
    @GetMapping
    fun getUser(): Response<User> {
        val user = User(
            username = "faker",
            password = "Fake@123"
        )
        return Response(success = true, data = user)
    }

    @PostMapping
    fun createUser(@RequestBody user: User): Response<User> {
        if (user.createTime == null) {
            user.createTime = LocalDateTime.now().plusHours(1)
        }
        if (user.updateTime == null) {
            user.updateTime = LocalDateTime.now().plusHours(1)
        }
        return Response(success = true, data = user)
    }
}

data class Response<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null,
)

data class User(
    var username: String,
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var password: String,
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var createTime: LocalDateTime? = null,
    var updateTime: LocalDateTime? = null,
)