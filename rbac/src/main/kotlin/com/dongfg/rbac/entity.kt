package com.dongfg.rbac

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.*

@Table(name = "user")
@Entity
class User(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(columnDefinition = "bigint not null")
    var roleId: Long,
    @Column(columnDefinition = "varchar(100) not null")
    var username: String,
    @Column(columnDefinition = "varchar(100) not null")
    var password: String,
    @Column(columnDefinition = "boolean not null")
    var enabled: Boolean? = true
) {
    override fun toString(): String {
        return "User(id=$id, roleId=$roleId, username='$username', password='$password', enabled=$enabled)"
    }
}

@Table(name = "role")
@Entity
class Role(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(columnDefinition = "varchar(100) not null")
    var name: String
) {
    override fun toString(): String {
        return "Role(id=$id, name='$name')"
    }
}

@Table(name = "authority")
@Entity
class Authority(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(columnDefinition = "bigint not null")
    var roleId: Long,
    @Column(columnDefinition = "varchar(100) not null")
    var authority: String
) {
    override fun toString(): String {
        return "Authority(id=$id, roleId=$roleId, authority='$authority')"
    }
}

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}

interface RoleRepository : JpaRepository<Role, Long>
interface AuthorityRepository : JpaRepository<Authority, Long> {
    fun findAllByRoleId(roleId: Long): List<Authority>
}