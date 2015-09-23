package com.mynote.dto;

import com.mynote.config.validation.ValidationGroupB;
import com.mynote.config.validation.ValidationGroupC;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public abstract class CommonUserDetails {

    @NotBlank
    @Length(min = 1, max = 255, groups = ValidationGroupB.class)
    @SafeHtml(groups = ValidationGroupC.class)
    protected String firstName;

    @NotBlank
    @Length(min = 1, max = 255, groups = ValidationGroupB.class)
    @SafeHtml(groups = ValidationGroupC.class)
    protected String lastName;

    @NotBlank
    @Length(min = 5, max = 191, groups = ValidationGroupB.class)
    @Email(
            regexp = "(?i)^([\\w-]+(?:\\.[\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$",
            groups = ValidationGroupC.class
    )
    protected String email;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommonUserDetails that = (CommonUserDetails) o;

        return !(email != null ? !email.equals(that.email) : that.email != null);
    }

    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }
}
