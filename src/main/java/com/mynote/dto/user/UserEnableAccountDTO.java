package com.mynote.dto.user;

import com.mynote.config.validation.ValidationGroupB;

import javax.validation.constraints.NotNull;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
public class UserEnableAccountDTO {

    @NotNull
    private Long id;

    @NotNull(groups = ValidationGroupB.class)
    private Boolean enabled;

    public UserEnableAccountDTO() {
    }

    public UserEnableAccountDTO(Long id, Boolean enabled) {
        this.id = id;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEnableAccountDTO that = (UserEnableAccountDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return !(enabled != null ? !enabled.equals(that.enabled) : that.enabled != null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (enabled != null ? enabled.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserEnableAccountDTO{" +
                "id=" + id +
                ", enabled=" + enabled +
                '}';
    }
}
