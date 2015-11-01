package com.mynote.test.unit.db;

import com.mynote.config.ApplicationProperties;
import com.mynote.config.db.DatabaseInitializer;
import com.mynote.exceptions.EmailAlreadyTakenException;
import com.mynote.exceptions.LoginAlreadyTakenException;
import com.mynote.models.User;
import com.mynote.models.UserRole;
import com.mynote.repositories.UserRepository;
import com.mynote.repositories.UserRoleRepository;
import com.mynote.services.UserRoleService;
import com.mynote.test.unit.services.AbstractServiceTest;
import com.mynote.test.utils.db.DBUnitHelper;
import org.dbunit.DatabaseUnitException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class DatabaseInitializerTests extends AbstractServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializerImpl;
    
    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private DBUnitHelper dbUnitHelper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Before
    public void setup() throws DatabaseUnitException, SQLException, EmailAlreadyTakenException, LoginAlreadyTakenException {
        dbUnitHelper.deleteUsersFromDb();
        databaseInitializerImpl.init();
    }

    @Test
    public void init_SystemAdministratorAndUserRolesAddedIntoDB() {
        String adminEmail = applicationProperties.getAdminEmail();
        User admin = userRepository.findByEmail(adminEmail);

        assertThat(admin.getEmail(), is(adminEmail));

        UserRole roleAdmin = userRoleRepository.findByRole(UserRoleService.ROLE_ADMIN);
        assertThat(roleAdmin.getRole(), is(UserRoleService.ROLE_ADMIN));

        UserRole roleUser = userRoleRepository.findByRole(UserRoleService.ROLE_USER);
        assertThat(roleUser.getRole(), is(UserRoleService.ROLE_USER));
    }
}
