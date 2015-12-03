package com.mynote.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mynote.models.User;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public abstract class AbstractUserDTO {

    @JsonIgnore
    protected User user;

    public AbstractUserDTO() {
        this.user = new User();
    }

    public AbstractUserDTO(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
