package com.mynote.utils;

import com.google.common.collect.Iterables;
import com.mynote.dto.user.UserRoleDTO;
import com.mynote.models.UserRole;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import static com.mynote.utils.RoleComparator.ROLE_PREFIX;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserRoleDtoUtil {

    public static UserRoleDTO[] convertAuthorities(Collection<? extends GrantedAuthority> authorities) {
        UserRoleDTO[] userRoleDTOArray = new UserRoleDTO[Iterables.size(authorities)];
        int i = 0;

        for (GrantedAuthority role : authorities) {
            userRoleDTOArray[i] = new UserRoleDTO(role.getAuthority());
            i++;
        }
        Arrays.sort(userRoleDTOArray, new RoleDTOComparator());

        return userRoleDTOArray;
    }

    public static UserRoleDTO[] convert(Iterable<UserRole> userRoles) {
        UserRoleDTO[] userRoleDTOArray = new UserRoleDTO[Iterables.size(userRoles)];
        int i = 0;

        for (UserRole userRole : userRoles) {
            userRoleDTOArray[i] = new UserRoleDTO(userRole);
            i++;
        }
        Arrays.sort(userRoleDTOArray, new RoleDTOComparator());

        return userRoleDTOArray;
    }

    private static class RoleDTOComparator implements Comparator<UserRoleDTO> {

        @Override
        public int compare(UserRoleDTO o1, UserRoleDTO o2) {
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
}
