package com.mynote.test.utils;

import com.mynote.models.UserRole;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public class UserRoleTestUtils {

    public static UserRole getNotExistentRole() {
        return new UserRole("NON_EXISTENT_ROLE");
    }
}
