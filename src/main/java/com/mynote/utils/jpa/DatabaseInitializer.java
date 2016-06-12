package com.mynote.utils.jpa;

import com.mynote.config.ApplicationConfig;
import com.mynote.exceptions.EmailAlreadyTakenException;
import com.mynote.exceptions.LoginAlreadyTakenException;
import com.mynote.exceptions.UserRoleAlreadyExists;
import com.mynote.models.User;
import com.mynote.models.UserRole;
import com.mynote.services.UserRoleService;
import com.mynote.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import static com.mynote.services.UserRoleService.ROLE_ADMIN;
import static com.mynote.services.UserRoleService.ROLE_USER;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class DatabaseInitializer {

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    public void init() throws EmailAlreadyTakenException, LoginAlreadyTakenException {
        persistRoles();
        persistAdministrator();
    }

    private void persistRoles() {
        if (userRoleService.getRoleAdmin() == null) {
            try {
                userRoleService.addRole(new UserRole(ROLE_ADMIN));
            } catch (UserRoleAlreadyExists e) {
                e.printStackTrace();
            }
        }
        if (userRoleService.getRoleUser() == null) {
            try {
                userRoleService.addRole(new UserRole(ROLE_USER));
            } catch (UserRoleAlreadyExists e) {
                e.printStackTrace();
            }
        }
    }

    private void persistAdministrator() throws EmailAlreadyTakenException, LoginAlreadyTakenException {
        if (userService.getSystemAdministrator() == null) {
            User admin = new User(applicationConfig.getAdminLogin(), applicationConfig.getAdminEmail());

            admin.setFirstName("System");
            admin.setLastName("Administrator");
            admin.setPassword(applicationConfig.getAdminPassword());
            userService.addAdministrator(admin);
        }
    }
}
