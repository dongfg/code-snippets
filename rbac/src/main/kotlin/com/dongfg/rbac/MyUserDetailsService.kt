package com.dongfg.rbac

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.io.Serializable


@Service
class MyUserDetailsService : UserDetailsService {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var roleRepository: RoleRepository

    @Autowired
    private lateinit var authorityRepository: AuthorityRepository

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException("can not find user '$username'")
        val role = roleRepository.findByIdOrNull(user.roleId)!!
        val authorities = authorityRepository.findAllByRoleId(user.roleId)
        return MyUserDetails(user, buildList {
            add(SimpleGrantedAuthority("ROLE_" + role.name))
            authorities.forEach { auth ->
                add(SimpleGrantedAuthority(auth.authority))
            }
        }.toMutableList())
    }
}

class MyUserDetails(
    private val user: User,
    private val authorityList: MutableCollection<out GrantedAuthority>
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorityList

    override fun getUsername() = user.username

    override fun getPassword(): String? = null

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true

    override fun toString(): String {
        return "MyUserDetails(user=$user, authorityList=$authorityList)"
    }
}

@Component
class MyPermissionEvaluator : PermissionEvaluator {
    override fun hasPermission(authentication: Authentication?, targetDomainObject: Any?, permission: Any?): Boolean {
        if (targetDomainObject != null && targetDomainObject is String) {
            return targetDomainObject.uppercase().contains("READ")
        }
        return false
    }

    override fun hasPermission(authentication: Authentication?, targetId: Serializable?, targetType: String?, permission: Any?): Boolean {
        println(targetId)
        return false
    }

}