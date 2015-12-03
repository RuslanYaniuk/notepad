package com.mynote.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mynote.dto.user.constraints.IdConstraint;
import com.mynote.dto.user.constraints.PasswordConstraint;

import javax.validation.Valid;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserResetPasswordDTO extends AbstractUserDTO {

    @Valid
    @JsonIgnore
    private IdConstraint idConstraint = new IdConstraint(this.user);

    @Valid
    @JsonIgnore
    private PasswordConstraint passwordConstraint = new PasswordConstraint(this.user);

    // Json building getters and setters
    public Long getId() {
        return user.getId();
    }

    public void setId(Long id) {
        user.setId(id);
    }

    public String getPassword() {
        return user.getPassword();
    }

    public void setPassword(String password) {
        user.setPassword(password);
    }

    // Validation getters
    public IdConstraint getIdConstraint() {
        return idConstraint;
    }

    public PasswordConstraint getPasswordConstraint() {
        return passwordConstraint;
    }
}
