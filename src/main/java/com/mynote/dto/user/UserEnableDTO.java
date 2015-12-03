package com.mynote.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mynote.dto.user.constraints.EnableConstraint;
import com.mynote.dto.user.constraints.IdConstraint;

import javax.validation.Valid;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
public class UserEnableDTO extends AbstractUserDTO {

    @Valid
    @JsonIgnore
    private IdConstraint idConstraint = new IdConstraint(this.user);

    @Valid
    @JsonIgnore
    private EnableConstraint enableConstraint = new EnableConstraint(this.user);

    // Json building getters and setters
    public Long getId() {
        return user.getId();
    }

    public void setId(Long id) {
        user.setId(id);
    }

    public Boolean getEnabled() {
        return user.isEnabled();
    }

    public void setEnabled(Boolean enabled) {
        user.setEnabled(enabled);
    }

    // Validation getters
    public IdConstraint getIdConstraint() {
        return idConstraint;
    }

    public EnableConstraint getEnableConstraint() {
        return enableConstraint;
    }
}
