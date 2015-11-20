package com.mynote.test.unit.services;

import com.google.common.collect.Lists;
import com.mynote.config.web.ApplicationProperties;
import com.mynote.dto.user.*;
import com.mynote.exceptions.*;
import com.mynote.models.User;
import com.mynote.models.UserRole;
import com.mynote.services.UserService;
import com.mynote.test.utils.db.DBUnitHelper;
import org.dbunit.DatabaseUnitException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

import static com.mynote.test.utils.UserDtoUtil.createSimpleUserRegistrationDTO;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserServiceTests extends AbstractServiceTest {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private UserService userService;

    @Autowired
    private DBUnitHelper dbUnitHelper;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Before
    public void setup() throws DatabaseUnitException, SQLException, FileNotFoundException {
        dbUnitHelper.deleteUsersFromDb();
        dbUnitHelper.cleanInsertUsersIntoDb();
    }

    @Test
    public void getSystemAdministrator_SystemAdministratorReturned() {
        User admin = userService.getSystemAdministrator();

        assertThat(admin.getEmail(), is(applicationProperties.getAdminEmail()));
    }

    @Test
    public void addNewUser_ValidRegistrationUserDTO_UserSavedIntoDB() throws EmailAlreadyTakenException, LoginAlreadyTakenException, UserNotFoundException {
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();

        User user = userService.addNewUser(userRegistrationDTO);
        assertNotNull(user);
        verifyUser(userRegistrationDTO, user);

        user = userService.findUserByEmail(userRegistrationDTO.getEmail());
        verifyUser(userRegistrationDTO, user);
    }

    @Test
    public void addNewUser_PasswordAsPlainText_BCryptHashSaved() throws EmailAlreadyTakenException, LoginAlreadyTakenException, UserNotFoundException {
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();
        User user = userService.addNewUser(userRegistrationDTO);
        User savedUser = userService.findUserByEmail(user.getEmail());

        assertNotNull(savedUser.getPassword());
        assertThat(savedUser.getPassword(), not(userRegistrationDTO.getPassword()));

        assertTrue(BCrypt.checkpw(userRegistrationDTO.getPassword(), savedUser.getPassword()));
    }

    @Test
    public void addNewUser_EmailInMixedCase_EmailSavedInLowerCase() throws EmailAlreadyTakenException, LoginAlreadyTakenException {
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();

        userRegistrationDTO.setEmail("MixedCase@Email.COM");

        User user = userService.addNewUser(userRegistrationDTO);
        assertNotNull(user);

        assertThat(userRegistrationDTO.getEmail().toLowerCase(), is(user.getEmail()));

        verifyUser(userRegistrationDTO, user);
    }

    @Test
    public void addNewUser_LoginInMixedCase_LoginSavedInLowerCase() throws EmailAlreadyTakenException, LoginAlreadyTakenException {
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();

        userRegistrationDTO.setLogin("miXedcASElog@4in");

        User user = userService.addNewUser(userRegistrationDTO);
        assertNotNull(user);

        assertThat(userRegistrationDTO.getLogin().toLowerCase(), is(user.getLogin()));

        verifyUser(userRegistrationDTO, user);
    }

    @Test
    public void addNewUser_EmailAndLoginWithTrailingSpaces_SavedWithoutSpaces() throws EmailAlreadyTakenException, LoginAlreadyTakenException {
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();

        userRegistrationDTO.setLogin("      miXedcASElog@4in                    ");
        userRegistrationDTO.setEmail("             MixedCase@Email.COM   ");

        User user = userService.addNewUser(userRegistrationDTO);
        assertNotNull(user);

        assertThat(userRegistrationDTO.getLogin().toLowerCase().trim(), is(user.getLogin()));
        assertThat(userRegistrationDTO.getEmail().toLowerCase().trim(), is(user.getEmail()));
    }

    @Test
    public void addNewUser_FirstAndLastNamesWithTrailingSpaces_SavedWithoutSpaces() throws EmailAlreadyTakenException, LoginAlreadyTakenException {
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();

        userRegistrationDTO.setFirstName("      First   name                    ");
        userRegistrationDTO.setLastName("           lasT Name   ");

        User user = userService.addNewUser(userRegistrationDTO);
        assertNotNull(user);

        assertThat(userRegistrationDTO.getFirstName().trim(), is(user.getFirstName()));
        assertThat(userRegistrationDTO.getLastName().trim(), is(user.getLastName()));
    }

    @Test(expected = EmailAlreadyTakenException.class)
    public void addNewUser_AlreadyRegisteredEmail_ExceptionThrown() throws EmailAlreadyTakenException, LoginAlreadyTakenException, UserNotFoundException {
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();

        User user = userService.addNewUser(userRegistrationDTO);
        assertNotNull(user);
        verifyUser(userRegistrationDTO, user);

        userRegistrationDTO.setLogin("differentLogin");

        user = userService.addNewUser(userRegistrationDTO);
        assertNull(user);
    }

    @Test
    public void getAllUsersSortByLastNameDesc_ReturnedUsersListInNaturalOrder() throws DatabaseUnitException, FileNotFoundException, SQLException {
        int usersAmountInDb = dbUnitHelper.getUserDataSet().getTable("users").getRowCount();
        List<User> allUsers = userService.getAllUsersSortByLastNameDesc();
        List<User> users = Lists.newArrayList(allUsers);

        assertThat(users.size(), is(usersAmountInDb));

        for (int i = 0; i < users.size() - 1; i++) {
            char firstElement = users.get(i).getLastName().toLowerCase().charAt(0);
            char secondElement = users.get(i + 1).getLastName().toLowerCase().charAt(0);

            assertThat(firstElement, lessThanOrEqualTo(secondElement));
        }
    }

    @Test
    public void updateUser_ValidUserID_UserUpdated() throws UserNotFoundException, UserRoleNotFoundException {
        UserUpdateDTO originalUser = getUserUpdateDTO();

        originalUser.setFirstName("updated first name");
        originalUser.setLastName("updated last name");

        User user = userService.updateUser(originalUser);

        verifyUser(originalUser, user);
    }

    @Test
    public void updateUser_EmailWithTrailingSpaces_SavedWithoutSpaces() throws EmailAlreadyTakenException, LoginAlreadyTakenException, UserNotFoundException, UserRoleNotFoundException {
        UserUpdateDTO userUpdateDTO = getUserUpdateDTO();

        userUpdateDTO.setEmail("             MixedCase@Email.COM   ");

        User savedUser = userService.updateUser(userUpdateDTO);
        assertNotNull(savedUser);

        assertThat(userUpdateDTO.getEmail().toLowerCase().trim(), is(savedUser.getEmail()));
    }

    @Test
    public void updateUser_FirstAndLastNamesWithTrailingSpaces_SavedWithoutSpaces() throws EmailAlreadyTakenException, LoginAlreadyTakenException, UserNotFoundException, UserRoleNotFoundException {
        UserUpdateDTO userUpdateDTO = getUserUpdateDTO();

        userUpdateDTO.setFirstName("      First   naMe                    ");
        userUpdateDTO.setLastName("           lasT Name   ");

        User user = userService.updateUser(userUpdateDTO);
        assertNotNull(user);

        assertThat(userUpdateDTO.getFirstName().trim(), is(user.getFirstName()));
        assertThat(userUpdateDTO.getLastName().trim(), is(user.getLastName()));

    }

    @Test(expected = UserNotFoundException.class)
    public void updateUser_NotExistentID_ExceptionThrown() throws UserNotFoundException, UserRoleNotFoundException {
        UserUpdateDTO userDTO = new UserUpdateDTO(userService.findUserByEmail("andrew.anderson@mail.com"));

        userDTO.setId(999999999L);

        userDTO.setFirstName("updated first name");
        userDTO.setLastName("updated last name");

        userService.updateUser(userDTO);
    }

    @Test
    public void resetUserPassword_CorrectUserResetPasswordDTO_PasswordUpdated() throws UserNotFoundException {
        UserResetPasswordDTO resetPasswordDTO = new UserResetPasswordDTO(2L, "NewPassword");
        User user = userService.resetUserPassword(resetPasswordDTO);

        assertTrue(bCryptPasswordEncoder.matches(resetPasswordDTO.getPassword(), user.getPassword()));
    }

    @Test
    public void findById_ValidUserID_UserReturned() throws UserNotFoundException {
        User admin = userService.findUserById(1L);

        assertNotNull(admin);
        assertThat(admin.getLastName(), is("Administrator"));
    }

    @Test(expected = UserNotFoundException.class)
    public void deleteUser_ValidUserID_UserDeleted() throws UserNotFoundException, OperationNotPermitted {
        userService.deleteUser(new UserDeleteDTO(2L));

        assertNull(userService.findUserById(2L));
    }

    @Test(expected = OperationNotPermitted.class)
    public void deleteUser_SystemAdministratorID_ExceptionThrown() throws UserNotFoundException, OperationNotPermitted {
        userService.deleteUser(new UserDeleteDTO(1L));
    }

    @Test(expected = UserNotFoundException.class)
    public void deleteUser_NotExistentUserID_ExceptionThrown() throws UserNotFoundException, OperationNotPermitted {
        userService.deleteUser(new UserDeleteDTO(-456L));
    }

    @Test(expected = UserNotFoundException.class)
    public void findById_NotExistentUserID_ExceptionThrown() throws UserNotFoundException {
        userService.findUserById(-456L);
    }

    @Test
    public void addAdministrator_CorrectUserDTO_AdministratorAdded() throws EmailAlreadyTakenException, LoginAlreadyTakenException {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();

        registrationDTO.setLogin("newAdmin");
        registrationDTO.setEmail("newadmin@mail.com");
        registrationDTO.setFirstName("Admin First Name");
        registrationDTO.setLastName("Admin Last Name");
        registrationDTO.setPassword("Passw0rd");

        User admin = userService.addAdministrator(registrationDTO);

        assertTrue(admin.getRoles().contains(new UserRole("ROLE_ADMIN")));
    }

    @Test
    public void enableUserAccount_EnabledIsTRUE_AccountEnabled() throws UserNotFoundException, OperationNotPermitted {
        UserEnableAccountDTO enableAccountDTO = new UserEnableAccountDTO(6L, true);

        User user = userService.enableUserAccount(enableAccountDTO);

        assertThat(user.isEnabled(), is(enableAccountDTO.getEnabled()));
    }

    @Test
    public void enableUserAccount_EnabledIsFLSE_AccountDisabled() throws UserNotFoundException, OperationNotPermitted {
        UserEnableAccountDTO enableAccountDTO = new UserEnableAccountDTO(6L, true);

        User user = userService.enableUserAccount(enableAccountDTO);

        assertThat(user.isEnabled(), is(enableAccountDTO.getEnabled()));
    }

    @Test(expected = UserNotFoundException.class)
    public void enableUserAccount_NotExistentUserID_ExceptionThrown() throws UserNotFoundException, OperationNotPermitted {
        UserEnableAccountDTO enableAccountDTO = new UserEnableAccountDTO(-456L, true);

        userService.enableUserAccount(enableAccountDTO);
    }

    @Test(expected = OperationNotPermitted.class)
    public void enableUserAccount_DisableSystemAdministrator_ExceptionThrown() throws UserNotFoundException, OperationNotPermitted {
        UserEnableAccountDTO enableAccountDTO = new UserEnableAccountDTO(1L, false);

        userService.enableUserAccount(enableAccountDTO);
    }

    @Test
    public void findUserByEmail_CorrectEmail_UserReturned() throws UserNotFoundException {
        User user = userService.findUserByEmail("user3@email.com");

        assertNotNull(user);
        assertThat(user.getEmail(), is("user3@email.com"));
        assertThat(user.getLogin(), is("user3"));
    }

    @Test(expected = UserNotFoundException.class)
    public void findUserByEmail_NotExistentEmail_ExceptionThrown() throws UserNotFoundException {
        userService.findUserByEmail("jonnyjo@mail.com");
    }

    @Test
    public void findByLoginOrEmail_CorrectParameters_UserReturned() throws UserNotFoundException {
        User user = userService.findByLoginOrEmail("user2", "user2@email.com");

        assertNotNull(user);

        user = userService.findByLoginOrEmail("user2", "not@correct.com");

        assertNotNull(user);
        assertThat(user.getEmail(), is("user2@email.com"));

        user = userService.findByLoginOrEmail("notExist", "user2@email.com");

        assertNotNull(user);
        assertThat(user.getLogin(), is("user2"));
    }

    @Test(expected = UserNotFoundException.class)
    public void findByLoginOrEmail_NotExistentEmailAndLogin_ExceptionThrown() throws UserNotFoundException {
        userService.findByLoginOrEmail("notValid", "no.name@mail.com");
    }

    private void verifyUser(UserRegistrationDTO userRegistrationDTO, User user) {
        assertThat(userRegistrationDTO.getEmail().toLowerCase().trim(), is(user.getEmail()));
        assertThat(userRegistrationDTO.getLogin().toLowerCase().trim(), is(user.getLogin()));

        assertThat(userRegistrationDTO.getFirstName().trim(), is(user.getFirstName()));
        assertThat(userRegistrationDTO.getLastName().trim(), is(user.getLastName()));
        assertTrue(user.isEnabled());
    }

    private void verifyUser(UserUpdateDTO userUpdateDTO, User user) {
        assertThat(userUpdateDTO.getEmail().toLowerCase().trim(), is(user.getEmail()));

        assertThat(userUpdateDTO.getFirstName().trim(), is(user.getFirstName()));
        assertThat(userUpdateDTO.getLastName().trim(), is(user.getLastName()));
    }

    private UserUpdateDTO getUserUpdateDTO() throws UserNotFoundException {
        UserUpdateDTO originalUser = new UserUpdateDTO();
        UserRoleDTO[] roleDTOs = new UserRoleDTO[1];

        roleDTOs[0] = new UserRoleDTO(1L, "ROLE_USER");

        originalUser.setId(2L);
        originalUser.setEmail("andrew.anderson@mail.com");
        originalUser.setFirstName("Andrew");
        originalUser.setLastName("Anderson");

        originalUser.setUserRoleDTOs(roleDTOs);
        return originalUser;
    }
}
