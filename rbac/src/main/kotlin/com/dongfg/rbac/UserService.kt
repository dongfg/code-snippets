package com.dongfg.rbac

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class UserService {
    fun allAccess() = "You got it"

    @PreAuthorize("hasRole('USER')")
    fun userAccessByRole() = "You got it"

    @PreAuthorize("hasAuthority('USER_ACCESS')")
    fun userAccessByAuthority() = "You got it"

    @PreAuthorize("hasRole('ADMIN')")
    fun adminAccessByRole() = "You got it"

    @PreAuthorize("hasAuthority('ADMIN_ACCESS')")
    fun adminAccessByAuthority() = "You got it"

    @PreAuthorize("hasPermission(#action, 'read')")
    fun permissionCheck(action: String) = "You got it"
}