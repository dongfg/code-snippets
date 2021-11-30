package com.dongfg.jackson_datetime

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class SerializeTests {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @Test
    fun defaultMapper() {
        val objectMapper = ObjectMapper().registerKotlinModule()
        assertThrows<InvalidDefinitionException> {
            println(
                objectMapper.writeValueAsString(
                    DateTimeResponse(
                        LocalDateTime.now(),
                        OffsetDateTime.now()
                    )
                )
            )
        }
    }

    @Test
    fun mapperWithJavaTimeModule() {
        val objectMapper = ObjectMapper().registerKotlinModule().registerModule(JavaTimeModule()).apply {
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        }
        println(
            objectMapper.writeValueAsString(
                DateTimeResponse(
                    LocalDateTime.now(),
                    OffsetDateTime.now()
                )
            )
        )
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
        println(
            objectMapper.writeValueAsString(
                DateTimeResponse(
                    LocalDateTime.now(),
                    OffsetDateTime.now()
                )
            )
        )
    }
}