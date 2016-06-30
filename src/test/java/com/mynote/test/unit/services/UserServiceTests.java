package com.mynote.test.unit.services;

import com.mynote.config.ApplicationConfig;
import com.mynote.config.Constants;
import com.mynote.exceptions.*;
import com.mynote.models.User;
import com.mynote.services.UserService;
import org.dbunit.DatabaseUnitException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

import static com.mynote.test.utils.UserTestUtils.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserServiceTests extends AbstractServiceTest {

    @Autowired
    private ApplicationConfig applicationProperties;

    @Autowired
    private UserService userService;

    @Before
    public void setup() throws DatabaseUnitException, SQLException, FileNotFoundException {
        dbUnit.deleteAllFixtures();

        dbUnit.insertUsers();
        dbUnit.insertRoles();
        dbUnit.insertUsersToRoles();
    }

    @Test
    public void addUser_NewLoginAndEmail_UserSavedIntoDB() throws EmailAlreadyTakenException, LoginAlreadyTakenException {
        User user = userService.addUser(createNonExistentUser());
        assertNotNull(user.getId());
    }

    @Test
    public void addUser_EmailInMixedCase_EmailSavedInLowerCase() throws EmailAlreadyTakenException, LoginAlreadyTakenException {
        User user = createNonExistentUser();

        user.setEmail("MixedCase@Email.COM");
        user = userService.addUser(user);
        assertThat(user.getEmail().toLowerCase(), is(user.getEmail()));
    }

    @Test
    public void addUser_LoginInMixedCase_LoginSavedInLowerCase() throws EmailAlreadyTakenException, LoginAlreadyTakenException {
        User user = createNonExistentUser("miXedcASElog@4in");

        user = userService.addUser(user);
        assertThat(user.getLogin().toLowerCase(), is(user.getLogin()));
    }

    @Test
    public void addUser_EmailAndLoginWithTrailingSpaces_SavedTrim() throws EmailAlreadyTakenException, LoginAlreadyTakenException {
        String login = "      miXedcASElog@4in                    ";
        String email = "             MixedCase@Email.COM   ";
        User user = createNonExistentUser(login, email);

        user = userService.addUser(user);
        assertThat(user.getLogin(), is(login.toLowerCase().trim()));
        assertThat(user.getEmail(), is(email.toLowerCase().trim()));
    }

    @Test
    public void addUser_FirstAndLastNamesWithTrailingSpaces_SavedTrim() throws EmailAlreadyTakenException, LoginAlreadyTakenException {
        String firstName = "      First   name                    ";
        String lastName = "           lasT Name   ";
        User user = createNonExistentUser();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user = userService.addUser(user);
        assertThat(user.getFirstName(), is(firstName.trim()));
        assertThat(user.getLastName(), is(lastName.trim()));
    }

    @Test(expected = EmailAlreadyTakenException.class)
    public void addUser_AlreadyRegisteredEmail_ExceptionThrown() throws EmailAlreadyTakenException, LoginAlreadyTakenException {
        User user = createNonExistentUser();

        user = userService.addUser(user);
        user = createNonExistentUser("differentLogin", user.getEmail());
        userService.addUser(user);
    }

    @Test(expected = LoginAlreadyTakenException.class)
    public void addUser_AlreadyRegisteredLogin_ExceptionThrown() throws EmailAlreadyTakenException, LoginAlreadyTakenException {
        User user = createNonExistentUser();

        user = userService.addUser(user);
        user = createNonExistentUser(user.getLogin(), "different@email.com");
        userService.addUser(user);
    }

    @Test
    public void addUser_NotEncryptedPassword_BCryptHashSaved() throws UserNotFoundException, EmailAlreadyTakenException, LoginAlreadyTakenException {
        String notEncryptedPass = "Pa$$w0rd";
        User user = createNonExistentUser();

        user.setPassword(notEncryptedPass);
        user = userService.addUser(user);
        assertThat(user.getPassword(), not(notEncryptedPass));
        assertTrue(BCrypt.checkpw(notEncryptedPass, user.getPassword()));
    }

    @Test
    public void addUser_EmailAndPassword_UserSaved() throws UserNotFoundException, EmailAlreadyTakenException, LoginAlreadyTakenException {
        User user = createNonExistentUser();
        String emailPrefix = "nonexistent";

        user = userService.addUser(user.getEmail(), user.getPassword());
        assertThat(user.getFirstName(), is(emailPrefix + UserService.USER_FIRST_NAME_SUFFIX));
        assertThat(user.getLastName(), is(emailPrefix + UserService.USER_LAST_NAME_SUFFIX));
        assertThat(user.getLogin(), is(emailPrefix + UserService.USER_LOGIN_SUFFIX));
    }

    @Test
    public void addAdministrator_NewLoginAndEmail_AdministratorAdded() throws EmailAlreadyTakenException, LoginAlreadyTakenException {
        User admin = createNonExistentUser("newAdmin", "newadmin@mail.com");

        admin.setEmail("newadmin@mail.com");
        admin.setFirstName("Admin First Name");
        admin.setLastName("Admin Last Name");
        admin.setPassword("Passw0rd");
        admin = userService.addAdministrator(admin);
        assertTrue(admin.getRoles().contains(Constants.ROLE_ADMIN));
    }

    @Test
    public void getAllUsersSortByLastNameDesc__UsersListInNaturalOrder() {
        List<User> users = userService.getAllUsersSortByLastNameDesc();

        assertThat(users, hasSize(7));
        for (int i = 0; i < users.size() - 1; i++) {
            char firstElement = users.get(i).getLastName().toLowerCase().charAt(0);
            char secondElement = users.get(i + 1).getLastName().toLowerCase().charAt(0);
            assertThat(firstElement, lessThanOrEqualTo(secondElement));
        }
    }

    @Test
    public void getSystemAdministrator_SystemAdministratorReturned() {
        User admin = userService.getSystemAdministrator();

        assertThat(admin.getEmail(), is(applicationProperties.getAdminEmail()));
    }

    @Test
    public void findById_ValidUserId_UserReturned() throws UserNotFoundException {
        User user2 = getUser2();
        User user = userService.findUserById(user2.getId());

        assertThat(user.getLogin(), is(user2.getLogin()));
    }

    @Test(expected = UserNotFoundException.class)
    public void findById_NotExistentUserId_ExceptionThrown() throws UserNotFoundException {
        userService.findUserById(createNonExistentUser().getId());
    }

    @Test
    public void findUserByEmail_CorrectEmail_UserReturned() throws UserNotFoundException {
        User user = userService.findUserByEmail("user3@email.com");

        assertThat(user.getEmail(), is("user3@email.com"));
        assertThat(user.getLogin(), is("user3"));
    }

    @Test(expected = UserNotFoundException.class)
    public void findUserByEmail_NotExistentEmail_ExceptionThrown() throws UserNotFoundException {
        userService.findUserByEmail(createNonExistentUser().getEmail());
    }

    @Test
    public void findUserByLogin_CorrectLogin_UserReturned() throws UserNotFoundException {
        User user = userService.findUserByLogin("user3");

        assertThat(user.getEmail(), is("user3@email.com"));
        assertThat(user.getLogin(), is("user3"));
    }

    @Test(expected = UserNotFoundException.class)
    public void findUserByLogin_NotExistentLogin_ExceptionThrown() throws UserNotFoundException {
        userService.findUserByLogin(createNonExistentUser().getLogin());
    }

    @Test
    public void findByLoginOrEmail_ValidLoginOrEmail_UserReturned() throws UserNotFoundException {
        String email = getUser2().getEmail();
        String login = getUser2().getLogin();
        User user = userService.findByLoginOrEmail(login, "not@correct.com");

        assertThat(user.getLogin(), is(login));
        user = userService.findByLoginOrEmail("notExist", email);
        assertThat(user.getEmail(), is(email));
    }

    @Test(expected = UserNotFoundException.class)
    public void findByLoginOrEmail_NotExistentEmailAndLogin_ExceptionThrown() throws UserNotFoundException {
        User user = createNonExistentUser();

        userService.findByLoginOrEmail(user.getLogin(), user.getLogin());
    }

    @Test
    public void updateUser_NewFirstNameAndLastName_UserUpdated() throws UserNotFoundException, UserRoleNotFoundException {
        String firstName = "updated first name";
        String lastName = "updated last name";
        User user = getUser2();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user = userService.updateUser(user);
        assertThat(user.getFirstName(), is(firstName));
        assertThat(user.getLastName(), is(lastName));
    }

    @Test
    public void updateUser_EmailWithTrailingSpaces_SavedLowercaseTrim() throws UserNotFoundException, UserRoleNotFoundException {
        String email = "             MixedCase@Email.COM   ";
        User user = getUser2();

        user.setEmail(email);
        user = userService.updateUser(user);
        assertThat(user.getEmail(), is(email.toLowerCase().trim()));
    }

    @Test
    public void updateUser_FirstAndLastNamesWithTrailingSpaces_SavedTrim() throws UserNotFoundException, UserRoleNotFoundException {
        String firstName = "      First   naMe                    ";
        String lastName = "           lasT Name   ";
        User user = getUser2();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user = userService.updateUser(user);
        assertThat(user.getFirstName(), is(firstName.trim()));
        assertThat(user.getLastName(), is(lastName.trim()));
    }

    @Test(expected = UserNotFoundException.class)
    public void updateUser_NotExistentUser_ExceptionThrown() throws UserNotFoundException, UserRoleNotFoundException {
        User user = createNonExistentUser();

        user.setFirstName("updated first name");
        user.setLastName("updated last name");
        userService.updateUser(user);
    }

    @Test
    public void updateUserRoles_UserWithNewRolesSet_RolesUpdated() throws UserRoleNotFoundException, UserNotFoundException {
        User user = getUser2();

        user.addRole(Constants.ROLE_ADMIN);
        user = userService.updateUserRoles(user);
        assertThat(user.getRoles(), hasItem(Constants.ROLE_ADMIN));
    }

    @Test(expected = UserNotFoundException.class)
    public void deleteUser_ValidUserId_UserDeleted() throws OperationNotPermitted, UserNotFoundException {
        userService.deleteUser(getUser2());
        userService.findUserById(2L);
    }

    @Test(expected = OperationNotPermitted.class)
    public void deleteUser_UserTypeSysAdmin_ExceptionThrown() throws OperationNotPermitted, UserNotFoundException {
        userService.deleteUser(getUserAdmin());
    }

    @Test
    public void resetUserPassword_ValidNewPassword_PasswordUpdated() throws UserNotFoundException {
        String newPassword = "newPa$$w0rd";
        User user = getUser2();

        user.setPassword(newPassword);
        user = userService.resetUserPassword(user);
        assertTrue(BCrypt.checkpw(newPassword, user.getPassword()));
    }

    @Test
    public void enableUserAccount_EnabledIsTRUE_AccountEnabled() throws UserNotFoundException, OperationNotPermitted {
        User user = getUser2();

        user.setEnabled(true);
        user = userService.enableUserAccount(user);
        assertTrue(user.isEnabled());
    }

    @Test
    public void enableUserAccount_EnabledIsFLSE_AccountDisabled() throws UserNotFoundException, OperationNotPermitted {
        User user = getUser2();

        user.setEnabled(false);
        user = userService.enableUserAccount(user);
        assertFalse(user.isEnabled());
    }

    @Test(expected = UserNotFoundException.class)
    public void enableUserAccount_NotExistentUserID_ExceptionThrown() throws UserNotFoundException, OperationNotPermitted {
        User user = createNonExistentUser();

        userService.enableUserAccount(user);
    }

    @Test(expected = OperationNotPermitted.class)
    public void enableUserAccount_DisableSystemAdministrator_ExceptionThrown() throws UserNotFoundException, OperationNotPermitted {
        userService.enableUserAccount(getUserAdmin());
    }
}
