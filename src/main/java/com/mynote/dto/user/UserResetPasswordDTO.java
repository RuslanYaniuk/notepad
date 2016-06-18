package com.mynote.dto.user;

import com.mynote.utils.validation.annotations.Password;

import javax.validation.constraints.NotNull;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserResetPasswordDTO extends AbstractUserDTO {

    @NotNull
    public Long getId() {
        return user.getId();
    }

    public void setId(Long id) {
        user.setId(id);
    }

    @Password
    public String getPassword() {
        return user.getPassword();
    }

    public void setPassword(String password) {
        user.setPassword(password);
    }
}
