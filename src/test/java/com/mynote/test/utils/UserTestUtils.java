package com.mynote.test.utils;

import com.google.common.collect.Sets;
import com.mynote.config.Constants;
import com.mynote.models.User;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public class UserTestUtils {

    public static User createNonExistentUser() {
        return createNonExistentUser("nonExistentLogin", "nonexistent@email.com");
    }

    public static User createNonExistentUser(String login) {
        return createNonExistentUser(login, "nonexistent@email.com");
    }

    public static User createNonExistentUser(String login, String email) {
        User user = new User(login, email);

        user.setId(99999999999L);
        user.setFirstName("Non Existent FirstName");
        user.setLastName("Non existent LastName");
        user.setPassword("Pa$$w0rd");
        return user;
    }

    public static User getUser2() {
        User user = new User("user2login", "user2@email.com");

        user.setId(2L);
        user.setFirstName("User2 FirstName");
        user.setLastName("User2 LastName");
        user.setEmail("user2@email.com");
        user.setPassword("$2a$10$4jP1RZjEdoHFd.f2RMSP1utv3semRaKYi0NOsbO8FnM0cZipCsvOe");
        user.setEnabled(true);
        user.addRole(Constants.ROLE_USER);
        return user;
    }

    public static User getUser3() {
        User user = new User("user3", "user3@email.com");

        user.setId(3L);
        user.setFirstName("User3 FirstName");
        user.setLastName("User3 LastName");
        user.setEmail("user3@email.com");
        user.setPassword("$2a$10$4jP1RZjEdoHFd.f2RMSP1utv3semRaKYi0NOsbO8FnM0cZipCsvOe");
        user.setEnabled(true);
        user.addRole(Constants.ROLE_USER);
        return user;
    }

    public static User getUserAdmin() {
        User user = new User("admin", "admin@email.com");

        user.setId(1L);
        user.setPassword("Passw0rd");
        user.setRoles(Sets.newHashSet(Constants.ROLE_ADMIN, Constants.ROLE_USER));
        return user;
    }
}
