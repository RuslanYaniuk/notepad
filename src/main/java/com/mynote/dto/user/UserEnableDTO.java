package com.mynote.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mynote.dto.user.constraints.IdConstraint;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
public class UserEnableDTO extends AbstractUserDTO {

    private Boolean enabled;

    @Valid
    @JsonIgnore
    private IdConstraint idConstraint = new IdConstraint(this.user);

    // Json building getters and setters
    public Long getId() {
        return user.getId();
    }

    public void setId(Long id) {
        user.setId(id);
    }

    @NotNull
    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
        if (enabled == null) {
            enabled = false;
        }
        user.setEnabled(enabled);
    }

    // Validation getters
    public IdConstraint getIdConstraint() {
        return idConstraint;
    }
}
