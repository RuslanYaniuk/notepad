package com.mynote.dto.user;

import com.mynote.models.UserRole;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public class UserUpdateRolesDTO extends AbstractUserDTO {

    @NotNull
    public Long getId() {
        return user.getId();
    }

    public void setId(Long id) {
        user.setId(id);
    }

    @NotEmpty
    public Set<UserRole> getUserRoles() {
        return user.getRoles();
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        user.setRoles(userRoles);
    }
}
