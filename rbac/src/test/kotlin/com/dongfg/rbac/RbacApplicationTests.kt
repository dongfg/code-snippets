package com.dongfg.rbac

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrowsExactly
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.test.context.support.WithUserDetails

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class RbacApplicationTests {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var roleRepository: RoleRepository

    @Autowired
    private lateinit var authorityRepository: AuthorityRepository

    @Autowired
    private lateinit var userService: UserService

    private val defaultPassword = BCryptPasswordEncoder().encode("123456")

    @BeforeAll
    fun prepare() {
        val userRole = Role(name = "USER")
        val adminRole = Role(name = "ADMIN")
        roleRepository.save(userRole)
        roleRepository.save(adminRole)

        userRepository.saveAll(
            arrayListOf(
                User(
                    username = "user",
                    password = defaultPassword,
                    roleId = userRole.id!!
                ), User(
                    username = "admin",
                    password = defaultPassword,
                    roleId = adminRole.id!!
                )
            )
        )

        authorityRepository.saveAll(
            arrayListOf(
                Authority(authority = "USER_ACCESS", roleId = userRole.id!!),
                Authority(authority = "ADMIN_ACCESS", roleId = adminRole.id!!)
            )
        )
    }

    @AfterAll
    fun cleanup() {
        userRepository.deleteAll()
        roleRepository.deleteAll()
        authorityRepository.deleteAll()
    }

    @Test
    fun prepareCheck() {
        assertEquals(2, userRepository.count())
        assertEquals(2, roleRepository.count())
        assertEquals(2, authorityRepository.count())
    }

    @Test
    @WithUserDetails("user")
    fun runAsUser() {
        if (SecurityContextHolder.getContext().authentication.principal is MyUserDetails) {
            println(SecurityContextHolder.getContext().authentication.principal)
        }
        userService.allAccess()
        userService.userAccessByRole()
        userService.userAccessByAuthority()
        assertThrowsExactly(org.springframework.security.access.AccessDeniedException::class.java) {
            userService.adminAccessByRole()
        }
        assertThrowsExactly(org.springframework.security.access.AccessDeniedException::class.java) {
            userService.adminAccessByAuthority()
        }
    }

    @Test
    @WithUserDetails("admin")
    fun runAsAdmin() {
        if (SecurityContextHolder.getContext().authentication.principal is MyUserDetails) {
            println(SecurityContextHolder.getContext().authentication.principal)
        }
        userService.allAccess()
        userService.adminAccessByRole()
        userService.adminAccessByAuthority()
        assertThrowsExactly(org.springframework.security.access.AccessDeniedException::class.java) {
            userService.userAccessByRole()
        }
        assertThrowsExactly(org.springframework.security.access.AccessDeniedException::class.java) {
            userService.userAccessByAuthority()
        }
    }

    @Test
    @WithUserDetails("user")
    fun permissionCheck() {
        userService.permissionCheck("Let Me Read")
    }

}
