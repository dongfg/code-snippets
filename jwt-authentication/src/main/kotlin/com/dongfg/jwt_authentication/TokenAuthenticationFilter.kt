package com.dongfg.jwt_authentication

import com.dongfg.jwt_authentication.web.AdminUserDetailsService
import com.dongfg.jwt_authentication.web.ApiUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenHeaders {
    companion object {
        const val API_TOKEN = "Authorization"
        const val ADMIN_TOKEN = "X-Auth-Token"
    }
}

/**
 * 执行认证逻辑
 */
class TokenAuthenticationFilter(private val tokenHeader: String = TokenHeaders.API_TOKEN, private val authenticationManager: AuthenticationManager) :
    OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val token = request.getHeader(tokenHeader)
        if (token != null) {
            val authentication = TokenAuthentication(tokenHeader, token)
            val authResult = authenticationManager.authenticate(authentication)
            if (authResult != null) {
                SecurityContextHolder.getContext().authentication = authResult
            }
        }
        filterChain.doFilter(request, response)
    }
}

/**
 * 认证载体： 认证信息，是否已认证
 */
class TokenAuthentication(private val principal: Any, private val credentials: Any, authorityList: Collection<GrantedAuthority>? = null) :
    AbstractAuthenticationToken(authorityList) {
    override fun getPrincipal() = principal
    override fun getCredentials() = credentials
}

/**
 * 具体认证方法
 */
@Component
class TokenAuthenticationProvider : AuthenticationProvider {
    @Autowired
    private lateinit var apiUserDetailsService: ApiUserDetailsService

    @Autowired
    private lateinit var adminUserDetailsService: AdminUserDetailsService

    override fun authenticate(authentication: Authentication): Authentication? {
        if (authentication.credentials != "fake") {
            return null
        }

        val fakeUsernameFromToken = "faker"

        val user = if (authentication.principal == TokenHeaders.API_TOKEN) {
            apiUserDetailsService.loadUserByUsername(fakeUsernameFromToken)
        } else {
            adminUserDetailsService.loadUserByUsername(fakeUsernameFromToken)
        }

        return TokenAuthentication(user, authentication.credentials, user.authorities).apply {
            isAuthenticated = true
            details = user
        }
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return authentication == TokenAuthentication::class.java
    }

}