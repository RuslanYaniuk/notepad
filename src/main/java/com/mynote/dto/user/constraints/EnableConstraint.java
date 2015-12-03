package com.mynote.dto.user.constraints;

import com.mynote.config.validation.ValidationGroupB;
import com.mynote.dto.user.AbstractUserDTO;
import com.mynote.models.User;

import javax.validation.constraints.NotNull;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public class EnableConstraint extends AbstractUserDTO {

    public EnableConstraint(User user) {
        super(user);
    }

    @NotNull(groups = ValidationGroupB.class)
    public Boolean getEnabled() {
        return user.isEnabled();
    }
}
