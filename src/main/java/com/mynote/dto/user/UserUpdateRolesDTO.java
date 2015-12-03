package com.mynote.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mynote.dto.user.constraints.IdConstraint;
import com.mynote.models.UserRole;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.Set;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public class UserUpdateRolesDTO extends AbstractUserDTO {

    @Valid
    @JsonIgnore
    private IdConstraint idConstraint = new IdConstraint(this.user);

    // Json building getters/setters and validation
    public void setId(Long id) {
        user.setId(id);
    }

    public Long getId() {
        return user.getId();
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        user.setRoles(userRoles);
    }

    @NotEmpty
    public Set<UserRole> getUserRoles() {
        return user.getRoles();
    }

    // Validation getters
    public IdConstraint getIdConstraint() {
        return idConstraint;
    }

    public void setIdConstraint(IdConstraint idConstraint) {
        this.idConstraint = idConstraint;
    }
}
