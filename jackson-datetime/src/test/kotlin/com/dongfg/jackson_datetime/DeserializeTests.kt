package com.dongfg.jackson_datetime

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DeserializeTests {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @Test
    fun defaultMapper() {
        val objectMapper = ObjectMapper().registerKotlinModule()
        val jsonBody = """{"localDateTime":"2021-11-30 17:16:02","offsetDateTime":"2021-11-30 17:16:02"}"""
        assertThrows<InvalidDefinitionException> {
            val res = objectMapper.readValue<DateTimeRequest>(jsonBody)
            println(res)
        }
    }

    @Test
    fun mapperWithJavaTimeModule() {
        val objectMapper = ObjectMapper().registerKotlinModule().registerModule(JavaTimeModule())
        val jsonBody = """{"localDateTime":"2021-11-30T17:26:28.442","offsetDateTime":"2021-11-30T17:26:28.443+08:00"}"""
        val res = objectMapper.readValue<DateTimeRequest>(jsonBody)
        println(res)
        println(res.offsetDateTime.offset.toString())
    }

    @Test
    fun mapperWithJavaTimeModuleSetTimeZone() {
        val objectMapper = ObjectMapper().registerKotlinModule().registerModule(JavaTimeModule()).apply {
            // setTimeZone(TimeZone.getDefault())
            setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
        }
        val jsonBody = """{"localDateTime":"2021-11-30T17:26:28.442","offsetDateTime":"2021-11-30T17:26:28.443+08:00"}"""
        val res = objectMapper.readValue<DateTimeRequest>(jsonBody)
        println(res)
        println(res.offsetDateTime.offset.toString())
    }

    @Test
    fun mapperWithJavaTimeModuleFormatter() {
        val objectMapper = ObjectMapper().registerKotlinModule().registerModule(JavaTimeModule().apply {
            addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(formatter))
            addSerializer(OffsetDateTime::class.java, object : JsonSerializer<OffsetDateTime>() {
                override fun serialize(value: OffsetDateTime?, gen: JsonGenerator?, serializers: SerializerProvider?) {
                    gen?.writeString(formatter.format(value))
                }
            })
        })
        val jsonBody = """{"localDateTime":"2021-11-30 17:16:02","offsetDateTime":"2021-11-30 17:16:02"}"""
        val res = objectMapper.readValue<DateTimeRequest>(jsonBody)
        println(res)
    }
}