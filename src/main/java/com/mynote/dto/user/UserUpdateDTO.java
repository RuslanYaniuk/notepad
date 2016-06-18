package com.mynote.dto.user;

import com.mynote.utils.validation.annotations.Email;
import com.mynote.utils.validation.annotations.Name;

import javax.validation.constraints.NotNull;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserUpdateDTO extends AbstractUserDTO {

    @NotNull
    public Long getId() {
        return user.getId();
    }

    public void setId(Long id) {
        user.setId(id);
    }

    @Name
    public String getFirstName() {
        return user.getFirstName();
    }

    public void setFirstName(String firstName) {
        user.setFirstName(firstName);
    }

    @Name
    public String getLastName() {
        return user.getLastName();
    }

    public void setLastName(String lastName) {
        user.setLastName(lastName);
    }

    @Email
    public String getEmail() {
        return user.getEmail();
    }

    public void setEmail(String email) {
        user.setEmail(email);
    }
}
