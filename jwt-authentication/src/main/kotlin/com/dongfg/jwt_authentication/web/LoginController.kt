package com.dongfg.jwt_authentication.web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @PostMapping("/api/login")
    fun post(@RequestBody request: LoginRequest): LoginResponse {
        try {
            // get your user by name
            if (!passwordEncoder.matches(request.password, passwordEncoder.encode("111111"))) {
                return LoginResponse(false, "Error password")
            }
        } catch (e: UsernameNotFoundException) {
            return LoginResponse(false, "User not exist")
        }
        return LoginResponse(true, data = "fake")
    }

    data class LoginRequest(
        val username: String,
        val password: String
    )

    data class LoginResponse(
        val success: Boolean,
        val message: String? = null,
        val data: String? = null
    )
}

// Entity here
class ApiUser(
    val id: Long? = null,
    val username: String,
    val mobile: String

) {
    override fun toString(): String {
        return "ApiUser(id=$id, username='$username', mobile='$mobile')"
    }
}

class ApiUserDetails(
    val user: ApiUser,
    private val authorityList: MutableCollection<out GrantedAuthority>
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorityList

    override fun getUsername() = user.username

    override fun getPassword(): String? = null

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true

}

@Service
class ApiUserDetailsService : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val user = ApiUser(id = 1, username = "Fake Api User", mobile = "13300001111")
        return ApiUserDetails(user, arrayListOf(SimpleGrantedAuthority("ROLE_API_USER")))
    }
}

// Entity here
class AdminUser(
    val id: Long? = null,
    val username: String,
    val mobile: String
) {
    override fun toString(): String {
        return "AdminUser(id=$id, username='$username', mobile='$mobile')"
    }
}

class AdminUserDetails(
    val user: AdminUser,
    private val authorityList: MutableCollection<out GrantedAuthority>
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorityList

    override fun getUsername() = user.username

    override fun getPassword(): String? = null

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true

}

@Service
class AdminUserDetailsService : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val user = AdminUser(id = 1, username = "Fake Admin User", mobile = "13200001111")
        return AdminUserDetails(user, arrayListOf(SimpleGrantedAuthority("ROLE_ADMIN_USER")))
    }
}