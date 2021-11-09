package com.dongfg.jackson_read_write

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AutoConfigureMockMvc
@SpringBootTest
class JacksonReadWriteApplicationTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `Write Only Field Should Not Return`() {
        mockMvc.perform(get("/"))
            .andExpect(jsonPath("$.data.username", `is`("faker")))
            .andExpect(content().string(not(containsString("password"))))
    }

    @Test
    fun `Read Only Field Can Not Set`() {
        val mockTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(JacksonConfig.DATE_TIME_FORMAT))
        val body = """
            {"username": "faker", "password": "Faker@123", "createTime": "$mockTime", "updateTime": "$mockTime"}
        """.trimIndent()
        mockMvc.perform(
            post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(jsonPath("$.data.createTime", not(`is`(mockTime))))
            .andExpect(jsonPath("$.data.updateTime", `is`(mockTime)))
    }
}
