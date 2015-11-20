package com.mynote.utils;

import com.mynote.models.UserRole;

import java.util.Comparator;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class RoleComparator implements Comparator<UserRole> {

    public static final String ROLE_PREFIX = "ROLE_";

    @Override
    public int compare(UserRole o1, UserRole o2) {
        String role1 = o1.getRole().replaceFirst(ROLE_PREFIX, "");
        String role2 = o2.getRole().replaceFirst(ROLE_PREFIX, "");

        if (role1.charAt(0) > role2.charAt(0)) {
            return 1;
        }
        if (role1.charAt(0) == role2.charAt(0)) {
            return 0;
        }
        if (role1.charAt(0) < role2.charAt(0)) {
            return -1;
        }
        return 0;
    }
}
