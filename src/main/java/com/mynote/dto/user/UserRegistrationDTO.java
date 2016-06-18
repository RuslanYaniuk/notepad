package com.mynote.dto.user;

import com.mynote.utils.validation.annotations.Email;
import com.mynote.utils.validation.annotations.Login;
import com.mynote.utils.validation.annotations.Name;
import com.mynote.utils.validation.annotations.Password;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserRegistrationDTO extends AbstractUserDTO {

    @Login
    public String getLogin() {
        return user.getLogin();
    }

    public void setLogin(String login) {
        user.setLogin(login);
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

    @Password
    public String getPassword() {
        return user.getPassword();
    }

    public void setPassword(String password) {
        user.setPassword(password);
    }
}
