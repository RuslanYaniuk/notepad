package com.mynote.dto.user;

import javax.validation.constraints.NotNull;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserDeleteDTO extends AbstractUserDTO {

    @NotNull
    public Long getId() {
        return user.getId();
    }

    public void setId(Long id) {
        user.setId(id);
    }
}
