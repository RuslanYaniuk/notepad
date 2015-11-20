package com.mynote.dto.user;

import com.mynote.models.User;

import java.util.Arrays;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserDTO extends CommonUserDetails {

    private Long id;
    private String login;
    private UserRoleDTO[] userRoleDTOs;
    private String registrationDateUTC;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.registrationDateUTC = user.getRegistrationDateUTC().toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public void setRegistrationDateUTC(String registrationDateUTC) {
        this.registrationDateUTC = registrationDateUTC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UserDTO userDTO = (UserDTO) o;

        return !(login != null ? !login.equals(userDTO.login) : userDTO.login != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (login != null ? login.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", userRoleDTOs=" + Arrays.toString(userRoleDTOs) + '\'' +
                ", registrationDateUTC='" + registrationDateUTC +
                '}';
    }
}
