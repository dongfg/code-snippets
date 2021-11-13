package com.dongfg.jwt_authentication

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.logout.LogoutFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.nio.charset.StandardCharsets

@EnableWebSecurity(debug = true)
class HttpSecurityConfig {

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Configuration
    @Order(1)
    class ApiWebSecurityConfigurationAdapter : WebSecurityConfigurerAdapter() {

        override fun configure(http: HttpSecurity) {
            http {
                sessionManagement {
                    sessionCreationPolicy = SessionCreationPolicy.STATELESS
                }
                securityMatcher("/api", "/api/**")
                authorizeRequests {
                    authorize("/api", hasRole("API_USER"))
                    authorize("/api/**", hasRole("API_USER"))
                }
                cors { } // by default uses a Bean by the name of corsConfigurationSource
                csrf { disable() }
                addFilterAfter(
                    TokenAuthenticationFilter(tokenHeader = TokenHeaders.API_TOKEN, authenticationManager = super.authenticationManager()),
                    LogoutFilter::class.java
                )
                exceptionHandling {
                    authenticationEntryPoint =
                        AuthenticationEntryPoint { _, response, e ->
                            response.status = HttpStatus.OK.value()
                            response.characterEncoding = StandardCharsets.UTF_8.displayName()
                            response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            response.writer.write("""{"success": false, "code": 999, "message": "${e.message}}"}""") // rewrite authException here
                        }
                }
            }
        }
    }

    @Configuration
    class AdminWebSecurityConfigurationAdapter : WebSecurityConfigurerAdapter() {

        override fun configure(http: HttpSecurity) {
            http {
                sessionManagement {
                    sessionCreationPolicy = SessionCreationPolicy.STATELESS
                }
                securityMatcher("/**")
                authorizeRequests {
                    authorize("/", permitAll)
                    authorize("/user/login", permitAll)
                    authorize("/admin", hasRole("ADMIN_USER"))
                    authorize("/admin/**", hasRole("ADMIN_USER"))
                    authorize(anyRequest, authenticated)
                }
                cors { } // by default uses a Bean by the name of corsConfigurationSource
                csrf { disable() }
                addFilterAfter(
                    TokenAuthenticationFilter(tokenHeader = TokenHeaders.ADMIN_TOKEN, authenticationManager = super.authenticationManager()),
                    LogoutFilter::class.java
                )
                exceptionHandling {
                    authenticationEntryPoint =
                        AuthenticationEntryPoint { _, response, e ->
                            response.status = HttpStatus.OK.value()
                            response.characterEncoding = StandardCharsets.UTF_8.displayName()
                            response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            response.writer.write("""{"success": false, "code": 999, "message": "${e.message}}"}""") // rewrite authException here
                        }
                }
            }
        }
    }

    @Bean
    fun corsConfigurationSource() = UrlBasedCorsConfigurationSource().apply {
        registerCorsConfiguration("/**", CorsConfiguration().apply {
            allowCredentials = true
            allowedMethods = arrayListOf("*")
            allowedHeaders = arrayListOf("*")
            allowedOriginPatterns = arrayListOf("http://localhost:[*]", "https://www.baidu.com")
        })
    }
}