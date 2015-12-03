package com.mynote.dto.user.constraints;

import com.mynote.dto.user.AbstractUserDTO;
import com.mynote.models.User;

import javax.validation.constraints.NotNull;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public class IdConstraint extends AbstractUserDTO {

    public IdConstraint(User user) {
        super(user);
    }

    @NotNull
    public Long getId() {
        return user.getId();
    }
}
