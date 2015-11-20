package com.mynote.dto.user;

import com.mynote.config.validation.ValidationGroupB;
import com.mynote.config.validation.ValidationGroupC;
import com.mynote.config.validation.annotations.Password;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserRegistrationDTO extends CommonUserDetails {

    @NotBlank
    @Length(min = 8, max = 191, groups = ValidationGroupB.class)
    @Pattern(regexp = "(?i)^[a-z0-9_-]{8,}$", groups = ValidationGroupC.class)
    private String login;

    @NotBlank
    @Length(min = 8, max = 100, groups = ValidationGroupB.class)
    @Password(groups = ValidationGroupC.class)
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UserRegistrationDTO that = (UserRegistrationDTO) o;

        return !(login != null ? !login.equals(that.login) : that.login != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (login != null ? login.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserRegistrationDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
