package com.mynote.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mynote.dto.user.constraints.IdConstraint;

import javax.validation.Valid;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserDeleteDTO extends AbstractUserDTO {

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

    // Validation getters
    public IdConstraint getIdConstraint() {
        return idConstraint;
    }
}
