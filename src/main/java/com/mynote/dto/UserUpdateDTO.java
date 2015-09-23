package com.mynote.dto;

import com.mynote.models.User;
import com.mynote.utils.UserRoleDtoUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserUpdateDTO extends CommonUserDetails {

    @NotNull
    private Long id;

    @Valid
    @NotNull
    private UserRoleDTO[] userRoleDTOs;

    private String registrationDateUTC;

    public UserUpdateDTO() {
    }

    public UserUpdateDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.userRoleDTOs = UserRoleDtoUtil.convert(user.getRoles());
        this.registrationDateUTC = user.getRegistrationDateUTC().toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRoleDTO[] getUserRoleDTOs() {
        return userRoleDTOs;
    }

    public void setUserRoleDTOs(UserRoleDTO[] userRoleDTOs) {
        this.userRoleDTOs = userRoleDTOs;
    }

    public String getRegistrationDateUTC() {
        return registrationDateUTC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UserUpdateDTO that = (UserUpdateDTO) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserUpdateDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", userRoleDTOs=" + Arrays.toString(userRoleDTOs) + '\'' +
                ", registrationDateUTC='" + registrationDateUTC +
                '}';
    }
}
