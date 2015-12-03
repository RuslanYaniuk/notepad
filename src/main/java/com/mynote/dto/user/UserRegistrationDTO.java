package com.mynote.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mynote.dto.user.constraints.*;

import javax.validation.Valid;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserRegistrationDTO extends AbstractUserDTO {

    @Valid
    @JsonIgnore
    private LoginConstraint loginConstraint = new LoginConstraint(this.user);

    @Valid
    @JsonIgnore
    private EmailConstraint emailConstraint = new EmailConstraint(this.user);

    @Valid
    @JsonIgnore
    private FirstNameConstraint firstNameConstraint = new FirstNameConstraint(this.user);

    @Valid
    @JsonIgnore
    private LastNameConstraint lastNameConstraint = new LastNameConstraint(this.user);

    @Valid
    @JsonIgnore
    private PasswordConstraint passwordConstraint = new PasswordConstraint(this.user);

    // Json building getters and setters
    public String getLogin() {
        return user.getLogin();
    }

    public void setLogin(String login) {
        user.setLogin(login);
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

    public String getPassword() {
        return user.getPassword();
    }

    public void setPassword(String password) {
        user.setPassword(password);
    }

    // Validation getters
    public LoginConstraint getLoginConstraint() {
        return loginConstraint;
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

    public PasswordConstraint getPasswordConstraint() {
        return passwordConstraint;
    }
}
