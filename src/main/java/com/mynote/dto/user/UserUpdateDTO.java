package com.mynote.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mynote.dto.user.constraints.EmailConstraint;
import com.mynote.dto.user.constraints.FirstNameConstraint;
import com.mynote.dto.user.constraints.IdConstraint;
import com.mynote.dto.user.constraints.LastNameConstraint;

import javax.validation.Valid;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserUpdateDTO extends AbstractUserDTO {

    @Valid
    @JsonIgnore
    private IdConstraint idConstraint = new IdConstraint(this.user);

    @Valid
    @JsonIgnore
    private EmailConstraint emailConstraint = new EmailConstraint(this.user);

    @Valid
    @JsonIgnore
    private FirstNameConstraint firstNameConstraint = new FirstNameConstraint(this.user);

    @Valid
    @JsonIgnore
    private LastNameConstraint lastNameConstraint = new LastNameConstraint(this.user);

    // Json building getters and setters
    public Long getId() {
        return user.getId();
    }

    public void setId(Long id) {
        user.setId(id);
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public void setFirstName(String firstName) {
        user.setFirstName(firstName);
    }

    public String getLastName() {
        return user.getLastName();
    }

    public void setLastName(String lastName) {
        user.setLastName(lastName);
    }

    public String getEmail() {
        return user.getEmail();
    }

    public void setEmail(String email) {
        user.setEmail(email);
    }

    // Validation getters
    public IdConstraint getIdConstraint() {
        return idConstraint;
    }

    public EmailConstraint getEmailConstraint() {
        return emailConstraint;
    }

    public FirstNameConstraint getFirstNameConstraint() {
        return firstNameConstraint;
    }

    public LastNameConstraint getLastNameConstraint() {
        return lastNameConstraint;
    }
}
