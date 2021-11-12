package com.dongfg.multiple_authentication

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MultipleAuthenticationApplicationTests {
    companion object {
        private val NORMAL_USER = httpBasic("user", "111111")
        private val ADMIN_USER = httpBasic("admin", "111111")
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun contextLoads() {
        mockMvc.perform(get("/"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(content().string("It works!"))
    }

    @Test
    fun `Access Public Api with user`() {
        mockMvc.perform(get("/public").with(NORMAL_USER))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
    }

    @Test
    fun `Access Public Api with admin`() {
        mockMvc.perform(get("/public").with(ADMIN_USER))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
    }

    @Test
    fun `Access Public Api with anonymous`() {
        mockMvc.perform(get("/public"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `Access Admin Api with user`() {
        mockMvc.perform(get("/admin").with(NORMAL_USER))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isForbidden)
    }

    @Test
    fun `Access Admin Api with admin`() {
        mockMvc.perform(get("/admin").with(ADMIN_USER))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
    }

    @Test
    fun `Access Admin Api with anonymous`() {
        mockMvc.perform(get("/admin"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `Access Api with user`() {
        mockMvc.perform(get("/api").with(NORMAL_USER))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
    }

    @Test
    fun `Access Api with admin`() {
        mockMvc.perform(get("/api").with(ADMIN_USER))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isForbidden)
    }

    @Test
    fun `Access Api with anonymous`() {
        mockMvc.perform(get("/api"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isUnauthorized)
    }
}
