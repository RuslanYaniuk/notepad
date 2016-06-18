package com.mynote.dto.user;

import javax.validation.constraints.NotNull;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
public class UserEnableDTO extends AbstractUserDTO {

    private Boolean enabled;

    @NotNull
    public Long getId() {
        return user.getId();
    }

    public void setId(Long id) {
        user.setId(id);
    }

    @NotNull
    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
        if (enabled == null) {
            enabled = false;
        }
        user.setEnabled(enabled);
    }
}
