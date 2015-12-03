package com.mynote.dto.user;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserLoginDTO extends AbstractUserDTO {

    // Json building getters and setters & validation
    @NotBlank
    public String getLogin() {
        return user.getLogin();
    }

    public void setLogin(String login) {
        user.setLogin(login);
    }

    @NotBlank
    public String getPassword() {
        return user.getPassword();
    }

    public void setPassword(String password) {
        user.setPassword(password);
    }
}
