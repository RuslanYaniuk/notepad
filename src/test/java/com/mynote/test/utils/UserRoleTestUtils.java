package com.mynote.test.utils;

import com.mynote.config.web.Constants;
import com.mynote.models.UserRole;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public class UserRoleTestUtils {

    public static UserRole getNotExistentRole() {
        return new UserRole("NON_EXISTENT_ROLE");
    }

    public static UserRole getRoleUser() {
        return Constants.ROLE_USER;
    }

    public static UserRole getRoleAdmin() {
        return Constants.ROLE_ADMIN;
    }

    public static SimpleGrantedAuthority getRoleAnonymous() {
        return Constants.ROLE_ANONYMOUS;
    }
}
