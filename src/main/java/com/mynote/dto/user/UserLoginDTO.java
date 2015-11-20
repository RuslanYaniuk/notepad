package com.mynote.dto.user;

import com.mynote.config.validation.ValidationGroupB;
import com.mynote.models.User;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserLoginDTO {

    @NotBlank
    private String login;

    @NotBlank(groups = ValidationGroupB.class)
    private String password;

    public UserLoginDTO() {
    }

    public UserLoginDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public UserLoginDTO(User user) {
        setLogin(user.getLogin());
        setPassword(user.getPassword());
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserLoginDTO{" +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
