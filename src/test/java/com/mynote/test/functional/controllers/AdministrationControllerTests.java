package com.mynote.test.functional.controllers;

import com.mynote.config.Constants;
import com.mynote.dto.user.*;
import com.mynote.exceptions.ValidationException;
import com.mynote.models.User;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static com.mynote.config.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static com.mynote.test.functional.controllers.RegistrationControllerTests.*;
import static com.mynote.test.utils.UserRoleTestUtils.getNotExistentRole;
import static com.mynote.test.utils.UserTestUtils.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class AdministrationControllerTests extends AbstractControllerTest {

    public static final String NOT_NULL_CODE = "NotNull";
    public static final String NOT_EMPTY_CODE = "NotEmpty";

    public static final String ID_FIELD_NAME = "id";
    public static final String USE_ROLES_FIELD_NAME = "userRoles";
    public static final String ENABLED_FIELD_NAME = "enabled";
    public static final String ID_OR_EMAIL_OR_LOGIN_FIELD_NAME = "idOrEmailOrLogin";

    private UserFindDTO userFindDTO;
    private User user2;
    private UserUpdateDTO userUpdateDTO;
    private UserUpdateRolesDTO updateRolesDTO;
    private UserDeleteDTO userDeleteDTO;
    private UserResetPasswordDTO resetPasswordDTO;
    private UserEnableDTO enableDTO;

    @Before
    public void setup() throws Exception {
        dbUnit.deleteAllFixtures();

        dbUnit.insertUsers();
        dbUnit.insertRoles();
        dbUnit.insertUsersToRoles();

        user2 = getUser2();
        userFindDTO = new UserFindDTO();
        userUpdateDTO = createUserUpdateDTO();
        updateRolesDTO = new UserUpdateRolesDTO();
        userDeleteDTO = new UserDeleteDTO();
        resetPasswordDTO = new UserResetPasswordDTO();
        enableDTO = new UserEnableDTO();
    }

    @Test
    public void listAllUsers_AllUsersListReturned() throws Exception {
        mockMvc.perform(get("/api/administration/list-all-users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)));
    }

    @Test
    public void findUser_CorrectUserId_UserDTOReturned() throws Exception {
        userFindDTO.setUser(user2);
        findUser(userFindDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.login", is(user2.getLogin())));
    }

    @Test
    public void findUser_NonExistentUserId_NotFoundErrorReturned() throws Exception {
        userFindDTO.setUser(createNonExistentUser());
        findUser(userFindDTO)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath($_MESSAGE_CODE, is("user.lookup.notFound")));
    }

    @Test
    public void findUser_NoParams_NotAcceptableErrorReturned() throws Exception {
        findUser(userFindDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath($_TYPE, is(ValidationException.class.getSimpleName())))
                .andExpect(jsonPath(getFieldErrorCodes(ID_OR_EMAIL_OR_LOGIN_FIELD_NAME), hasItem(NOT_BLANK_CODE)));
    }

    @Test
    public void getUserRoles__RolesReturned() throws Exception {
        mockMvc.perform(get("/api/administration/get-all-user-roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].role", not(StringUtils.EMPTY)))
                .andExpect(jsonPath("$.[1].role", not(StringUtils.EMPTY)));
    }

    @Test
    public void updateUser_ValidUserDetails_UserUpdated() throws Exception {
        userUpdateDTO.setFirstName("updated first Name");
        userUpdateDTO.setLastName(" updated last Name");
        userUpdateDTO.setEmail("new@email.com");
        updateUser(userUpdateDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath($_MESSAGE_CODE, is("user.update.success")));
    }

    @Test
    public void updateUser_NonValidFirstName_BadRequestReturned() throws Exception {
        for (String firstName : NULL_EMPTY_STRINGS) {
            userUpdateDTO.setFirstName(firstName);
            updateUserWithValidationViolation(userUpdateDTO)
                    .andExpect(jsonPath(getFieldErrorCodes(FIRST_NAME_FIELD_NAME), hasItem(NOT_BLANK_CODE)));
        }
        userUpdateDTO.setFirstName(CHARACTERS_300);
        updateUserWithValidationViolation(userUpdateDTO)
                .andExpect(jsonPath(getFieldErrorCodes(FIRST_NAME_FIELD_NAME), hasItem(LENGTH_CODE)));

        userUpdateDTO.setFirstName(HTML);
        updateUserWithValidationViolation(userUpdateDTO)
                .andExpect(jsonPath(getFieldErrorCodes(FIRST_NAME_FIELD_NAME), hasItem(NON_SAFE_HTML_CODE)));
    }

    @Test
    public void updateUser_NonValidLastName_BadRequestReturned() throws Exception {
        for (String lastName : NULL_EMPTY_STRINGS) {
            userUpdateDTO.setLastName(lastName);
            updateUserWithValidationViolation(userUpdateDTO)
                    .andExpect(jsonPath(getFieldErrorCodes(LAST_NAME_FIELD_NAME), hasItems(NOT_BLANK_CODE)));
        }
        userUpdateDTO.setLastName(CHARACTERS_300);
        updateUserWithValidationViolation(userUpdateDTO)
                .andExpect(jsonPath(getFieldErrorCodes(LAST_NAME_FIELD_NAME), hasItems(LENGTH_CODE)));

        userUpdateDTO.setLastName(HTML);
        updateUserWithValidationViolation(userUpdateDTO)
                .andExpect(jsonPath(getFieldErrorCodes(LAST_NAME_FIELD_NAME), hasItems(NON_SAFE_HTML_CODE)));
    }

    @Test
    public void updateUser_EmptyEmailORId_BadRequestReturned() throws Exception {
        userUpdateDTO.setEmail("");
        updateUserWithValidationViolation(userUpdateDTO)
                .andExpect(jsonPath(getFieldErrorCodes(EMAIL_FIELD_NAME), hasItem(NOT_BLANK_CODE)));

        userUpdateDTO.setId(null);
        updateUserWithValidationViolation(userUpdateDTO)
                .andExpect(jsonPath(getFieldErrorCodes(ID_FIELD_NAME), hasItem(NOT_NULL_CODE)));
    }

    @Test
    public void updateUserRoles_CorrectUserRoles_RolesUpdated() throws Exception {
        updateRolesDTO.setUser(user2);
        updateRolesDTO.getUserRoles().add(Constants.ROLE_ADMIN);
        updateUserRoles(updateRolesDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath($_MESSAGE_CODE, is("user.roles.updated")));
    }

    @Test
    public void updateUserRoles_NonValidUser_NotFoundReturned() throws Exception {
        updateRolesDTO.getUser().setId(null);
        updateRolesDTO.getUserRoles().add(Constants.ROLE_ADMIN);
        updateUserRoles(updateRolesDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath($_TYPE, is(ValidationException.class.getSimpleName())))
                .andExpect(jsonPath(getFieldErrorCodes(ID_FIELD_NAME), hasItem(NOT_NULL_CODE)));
    }

    @Test
    public void updateUserRoles_NonValidUserRoles_NotFoundReturned() throws Exception {
        updateRolesDTO.setUser(user2);
        updateRolesDTO.getUserRoles().add(getNotExistentRole());
        updateUserRoles(updateRolesDTO)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath($_MESSAGE_CODE, is("user.role.notFound")));

        updateRolesDTO.setUserRoles(null);
        updateUserRoles(updateRolesDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath($_TYPE, is(ValidationException.class.getSimpleName())))
                .andExpect(jsonPath(getFieldErrorCodes(USE_ROLES_FIELD_NAME), hasItem(NOT_EMPTY_CODE)));
    }

    @Test
    public void deleteUser_ValidUserDeleteDTO_UserDeleted() throws Exception {
        userDeleteDTO.setUser(user2);
        deleteUser(userDeleteDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath($_MESSAGE_CODE, is("user.account.deleted")));
    }

    @Test
    public void deleteUser_NotExistentUserID_NotFoundReturned() throws Exception {
        userDeleteDTO.setUser(createNonExistentUser());
        deleteUser(userDeleteDTO)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath($_MESSAGE_CODE, is("user.lookup.notFound")));
    }

    @Test
    public void deleteUser_SystemAdministratorID_BadRequestReturned() throws Exception {
        userDeleteDTO.setUser(getUserAdmin());
        deleteUser(userDeleteDTO)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath($_MESSAGE_CODE, is("action.notPermitted")));
    }

    @Test
    public void deleteUser_NullUserId_BadRequestReturned() throws Exception {
        userDeleteDTO.setUser(user2);
        userDeleteDTO.setId(null);
        deleteUser(userDeleteDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath($_TYPE, is(ValidationException.class.getSimpleName())))
                .andExpect(jsonPath(getFieldErrorCodes(ID_FIELD_NAME), hasItem(NOT_NULL_CODE)));
    }

    @Test
    public void resetUserPassword_CorrectUserResetPasswordDTO_PasswordChanged() throws Exception {
        resetPasswordDTO.setUser(user2);
        resetPasswordDTO.setPassword("NewPassw0rd@#");
        resetUserPassword(resetPasswordDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath($_MESSAGE_CODE, is("user.reset.password.success")));
    }

    @Test
    public void resetUserPassword_NonValidUserID_NotFoundReturned() throws Exception {
        resetPasswordDTO.setId(null);
        resetPasswordDTO.setPassword("NewPassw0rd@#");
        resetUserPasswordWithValidationViolation(resetPasswordDTO).
                andExpect(jsonPath(getFieldErrorCodes(ID_FIELD_NAME), hasItem(NOT_NULL_CODE)));

        resetPasswordDTO.setUser(user2);
        for (String password : NON_VALID_PASSWORDS) {
            resetPasswordDTO.setPassword(password);
            resetUserPasswordWithValidationViolation(resetPasswordDTO)
                    .andExpect(jsonPath(getFieldErrorCodes(PASSWORD_FIELD_NAME), hasItem(PASSWORD_CODE)));
        }
    }

    @Test
    public void enableUserAccount_EnabledIsTrue_AccountEnabled() throws Exception {
        enableDTO.setUser(user2);
        enableDTO.setEnabled(true);
        enableUserAccount(enableDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath($_MESSAGE_CODE, is("user.account.enabled")));
    }

    @Test
    public void enableUserAccount_EnabledIsFalse_AccountDisabled() throws Exception {
        enableDTO.setUser(user2);
        enableDTO.setEnabled(false);
        enableUserAccount(enableDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath($_MESSAGE_CODE, is("user.account.disabled")));
    }

    @Test
    public void enableUserAccount_NotExistentUserID_UserNotFoundReturned() throws Exception {
        enableDTO.setUser(createNonExistentUser());
        enableDTO.setEnabled(true);
        enableUserAccount(enableDTO)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath($_MESSAGE_CODE, is("user.lookup.notFound")));
    }

    @Test
    public void enableUserAccount_SystemAdministratorID_BadRequestReturned() throws Exception {
        enableDTO.setUser(getUserAdmin());
        enableDTO.setEnabled(true);
        enableUserAccount(enableDTO)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath($_MESSAGE_CODE, is("action.notPermitted")));
    }

    @Test
    public void enableUserAccount_NonValidRequest_BadRequestReturned() throws Exception {
        enableDTO.setUser(user2);
        enableDTO.setEnabled(null);
        enableUserAccount(enableDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath($_TYPE, is(ValidationException.class.getSimpleName())))
                .andExpect(jsonPath(getFieldErrorCodes(ENABLED_FIELD_NAME), hasItem(NOT_NULL_CODE)));

        enableDTO.setId(null);
        enableDTO.setEnabled(true);
        enableUserAccount(enableDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath($_TYPE, is(ValidationException.class.getSimpleName())))
                .andExpect(jsonPath(getFieldErrorCodes(ID_FIELD_NAME), hasItem(NOT_NULL_CODE)));
    }

    private ResultActions enableUserAccount(UserEnableDTO disableEnableDTO) throws Exception {
        return mockMvc.perform(post("/api/administration/enable-user-account")
                        .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                        .content(jacksonObjectMapper.writeValueAsString(disableEnableDTO))
        );
    }

    private ResultActions resetUserPassword(UserResetPasswordDTO resetPasswordDTO) throws Exception {
        return mockMvc.perform(post("/api/administration/reset-user-password")
                        .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                        .content(jacksonObjectMapper.writeValueAsString(resetPasswordDTO))
        );
    }

    private ResultActions resetUserPasswordWithValidationViolation(UserResetPasswordDTO resetPasswordDTO) throws Exception {
        return resetUserPassword(resetPasswordDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath($_TYPE, is(ValidationException.class.getSimpleName())));
    }

    private ResultActions deleteUser(UserDeleteDTO userDeleteDTO) throws Exception {
        return mockMvc.perform(delete("/api/administration/delete-user")
                        .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                        .content(jacksonObjectMapper.writeValueAsString(userDeleteDTO))
        );
    }

    private ResultActions updateUser(UserUpdateDTO userBeforeUpdateDTO) throws Exception {
        return mockMvc.perform(post("/api/administration/update-user")
                        .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                        .content(jacksonObjectMapper.writeValueAsString(userBeforeUpdateDTO))
        );
    }

    private ResultActions updateUserWithValidationViolation(UserUpdateDTO userUpdateDTO) throws Exception {
        return updateUser(userUpdateDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath($_TYPE, is(ValidationException.class.getSimpleName())));
    }

    private ResultActions findUser(UserFindDTO userFindDTO) throws Exception {
        return mockMvc.perform(post("/api/administration/find-user")
                        .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                        .content(jacksonObjectMapper.writeValueAsString(userFindDTO))
        );
    }

    private ResultActions updateUserRoles(UserUpdateRolesDTO userUpdateRolesDTO) throws Exception {
        return mockMvc.perform(post("/api/administration/update-user-roles")
                        .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                        .content(jacksonObjectMapper.writeValueAsString(userUpdateRolesDTO))
        );
    }

    private UserUpdateDTO createUserUpdateDTO() {
        UserUpdateDTO updateDTO = new UserUpdateDTO();

        updateDTO.setUser(user2);
        return updateDTO;
    }
}
