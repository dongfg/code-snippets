package com.dongfg.multiple_authentication

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@EnableWebSecurity
class MultiHttpSecurityConfig {

    @Bean
    fun passwordEncoder() = NoOpPasswordEncoder.getInstance()

    @Bean
    fun userDetailsService(): UserDetailsService {
        val users: User.UserBuilder = User.builder()
        val manager = InMemoryUserDetailsManager()
        manager.createUser(users.username("user").password("111111").roles("USER").build())
        manager.createUser(users.username("admin").password("111111").roles("ADMIN").build())
        return manager
    }

    @Configuration
    @Order(1)
    class PublicApiWebSecurityConfigurationAdapter : WebSecurityConfigurerAdapter() {
        override fun configure(http: HttpSecurity?) {
            http {
                securityMatcher("/public/**")
                authorizeRequests {
                    authorize(anyRequest, hasAnyRole("USER", "ADMIN"))
                }
                httpBasic { }
            }
        }
    }

    @Configuration
    @Order(2)
    class ApiWebSecurityConfigurationAdapter : WebSecurityConfigurerAdapter() {
        override fun configure(http: HttpSecurity?) {
            http {
                securityMatcher("/api/**")
                authorizeRequests {
                    authorize(anyRequest, hasRole("USER"))
                }
                httpBasic { }
            }
        }
    }

    @Configuration
    class AdminApiWebSecurityConfigurationAdapter : WebSecurityConfigurerAdapter() {
        override fun configure(http: HttpSecurity?) {
            http {
                securityMatcher("/admin/**")
                authorizeRequests {
                    authorize(anyRequest, hasRole("ADMIN"))
                }
                httpBasic { }
            }
        }
    }
}