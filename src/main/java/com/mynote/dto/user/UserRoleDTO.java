package com.mynote.dto.user;

import com.mynote.models.UserRole;

import javax.validation.constraints.NotNull;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserRoleDTO {

    @NotNull
    private Long id;

    private String role;

    public UserRoleDTO() {
    }

    public UserRoleDTO(UserRole userRole) {
        this.id = userRole.getId();
        this.role = userRole.getRole();
    }

    public UserRoleDTO(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    public UserRoleDTO(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

        UserRoleDTO that = (UserRoleDTO) o;

        return !(role != null ? !role.equals(that.role) : that.role != null);
    }

    @Override
    public int hashCode() {
        return role != null ? role.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserRoleDTO{" +
                "id=" + id +
                ", role='" + role + '\'' +
                '}';
    }
}
