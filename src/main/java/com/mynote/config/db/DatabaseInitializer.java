package com.mynote.config.db;

import com.mynote.config.web.ApplicationProperties;
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
    private ApplicationProperties appProperties;

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
            User admin = new User(appProperties.getAdminLogin(), appProperties.getAdminEmail());

            admin.setFirstName("System");
            admin.setLastName("Administrator");
            admin.setPassword(appProperties.getAdminPassword());

            userService.addAdministrator(admin);
        }
    }
}
