package com.mynote.test.unit.beans;

import com.mynote.config.ApplicationConfig;
import com.mynote.exceptions.EmailAlreadyTakenException;
import com.mynote.exceptions.LoginAlreadyTakenException;
import com.mynote.models.User;
import com.mynote.models.UserRole;
import com.mynote.repositories.jpa.UserRepository;
import com.mynote.repositories.jpa.UserRoleRepository;
import com.mynote.services.UserRoleService;
import com.mynote.test.unit.services.AbstractServiceTest;
import com.mynote.test.utils.DBUnitHelper;
import com.mynote.utils.jpa.DatabaseInitializer;
import org.dbunit.DatabaseUnitException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.sql.SQLException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@ContextConfiguration
@SuppressWarnings("SpringJavaAutowiringInspection")
public class DatabaseInitializerTests extends AbstractServiceTest {

    @Configuration
    static class TestConfig {

        @Bean(initMethod = "init")
        public DatabaseInitializer databaseInitializer() {
            return new DatabaseInitializer();
        }
    }

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private ApplicationConfig applicationProperties;

    @Autowired
    private DBUnitHelper dbUnitHelper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Before
    public void setup() throws DatabaseUnitException, SQLException {
        dbUnitHelper.deleteAllFixtures();
    }

    @Test
    public void init_SystemAdministratorAndUserRolesAddedIntoDB() throws EmailAlreadyTakenException, LoginAlreadyTakenException {
        databaseInitializer.init();

        String adminEmail = applicationProperties.getAdminEmail();
        User admin = userRepository.findByEmail(adminEmail);
        UserRole roleAdmin;
        UserRole roleUser;

        assertThat(admin.getEmail(), is(adminEmail));
        roleAdmin = userRoleRepository.findByRole(UserRoleService.ROLE_ADMIN);
        assertThat(roleAdmin.getRole(), is(UserRoleService.ROLE_ADMIN));
        roleUser = userRoleRepository.findByRole(UserRoleService.ROLE_USER);
        assertThat(roleUser.getRole(), is(UserRoleService.ROLE_USER));
    }
}
