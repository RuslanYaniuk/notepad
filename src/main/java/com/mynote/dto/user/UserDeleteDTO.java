package com.mynote.dto.user;

import javax.validation.constraints.NotNull;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserDeleteDTO {

    @NotNull
    private Long id;

    public UserDeleteDTO() {
    }

    public UserDeleteDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDeleteDTO that = (UserDeleteDTO) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserDeleteDTO{" +
                "id=" + id +
                '}';
    }
}
