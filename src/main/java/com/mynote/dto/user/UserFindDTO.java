package com.mynote.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserFindDTO extends AbstractUserDTO {

    public Long getId() {
        return user.getId();
    }

    public void setId(Long id) {
        user.setId(id);
    }

    public String getEmail() {
        return user.getEmail();
    }

    public void setEmail(String email) {
        user.setEmail(email);
    }

    public String getLogin() {
        return user.getLogin();
    }

    public void setLogin(String login) {
        user.setLogin(login);
    }

    // Validation getters
    @JsonIgnore
    @NotBlank
    public String getIdOrEmailOrLogin() {
        if (getId() != null) {
            return getId().toString();
        }
        if (getEmail() != null) {
            return getEmail();
        }
        if (getLogin() != null) {
            return getLogin();
        }
        return null;
    }
}
