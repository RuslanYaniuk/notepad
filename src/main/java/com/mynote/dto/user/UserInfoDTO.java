package com.mynote.dto.user;

import com.google.common.collect.Sets;
import com.mynote.models.User;
import com.mynote.models.UserRole;

import java.util.Set;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserInfoDTO extends AbstractUserDTO {

    public UserInfoDTO() {
    }

    public UserInfoDTO(User user) {
        super(user);
    }

    // Json building getters and setters
    public Long getId() {
        return user.getId();
    }

    public void setId(Long id) {
        user.setId(id);
    }

    public String getLogin() {
        return user.getLogin();
    }

    public void setLogin(String login) {
        user.setLogin(login);
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public void setFirstName(String firstName) {
        user.setFirstName(firstName);
    }

    public String getLastName() {
        return user.getLastName();
    }

    public void setLastName(String lastName) {
        user.setLastName(lastName);
    }

    public String getEmail() {
        return user.getEmail();
    }

    public void setEmail(String email) {
        user.setEmail(email);
    }

    public String getRegistrationDateUTC() {
        return user.getRegistrationDateUTC().toString();
    }

    public UserRole[] getUserRoles() {
        Set<UserRole> roleSet = user.getRoles();
        UserRole[] roles = new UserRole[roleSet.size()];

        return roleSet.toArray(roles);
    }

    public void setUserRoles(UserRole[] userRoles) {
        user.setRoles(Sets.newHashSet(userRoles));
    }
}
