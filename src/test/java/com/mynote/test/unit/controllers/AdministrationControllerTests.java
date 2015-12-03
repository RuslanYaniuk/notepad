package com.mynote.test.unit.controllers;

import com.mynote.dto.user.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultActions;

import static com.mynote.config.web.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static com.mynote.test.utils.UserRoleTestUtils.getNotExistentRole;
import static com.mynote.test.utils.UserRoleTestUtils.getRoleAdmin;
import static com.mynote.test.utils.UserTestUtils.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@Component
public class AdministrationControllerTests extends AbstractControllerTest {

    @Before
    public void setup() throws Exception {
        dbUnitHelper.deleteUsersFromDb();
        dbUnitHelper.cleanInsertUsersIntoDb();
    }

    @Test
    public void listAllUsers_AllUsersListReturned() throws Exception {
        mockMvc.perform(get("/api/administration/list-all-users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)));
    }

    @Test
    public void findUser_CorrectUserId_UserDTOReturned() throws Exception {
        UserFindDTO userFindDTO = new UserFindDTO();

        userFindDTO.setUser(getUser2());

        findUser(userFindDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.login", is("user2")));
    }

    @Test
    public void findUser_NotExistentUserId_NotFoundErrorReturned() throws Exception {
        UserFindDTO userFindDTO = new UserFindDTO();

        userFindDTO.setUser(createNonExistentUser());

        findUser(userFindDTO)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.lookup.notFound")));
    }

    @Test
    public void findUser_EmptyJson_NotAcceptableErrorReturned() throws Exception {
        UserFindDTO userFindDTO = new UserFindDTO();

        findUser(userFindDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is("NotBlank.userFindDTO.idOrEmailOrLogin")));
    }

    @Test
    public void getUserRoles_RolesReturned() throws Exception {
        mockMvc.perform(get("/api/administration/get-all-user-roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].role", not(StringUtils.EMPTY)))
                .andExpect(jsonPath("$.[1].role", not(StringUtils.EMPTY)));
    }

    @Test
    public void updateUser_FirstNameChanged_UserUpdated() throws Exception {
        UserUpdateDTO user = createUserUpdateDTO();

        user.setFirstName("updated first Name");

        updateUser(user)
                .andExpect(status().isOk())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.update.success")));
    }

    @Test
    public void updateUser_EmptyEmail_BadRequestReturned() throws Exception {
        UserUpdateDTO updateDTO = createUserUpdateDTO();

        updateDTO.setEmail("");

        updateUser(updateDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is("NotBlank.userUpdateDTO.emailConstraint.email")));
    }

    @Test
    public void updateUser_EmptyId_BadRequestReturned() throws Exception {
        UserUpdateDTO user = createUserUpdateDTO();

        user.setId(null);

        updateUser(user)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is("NotNull.userUpdateDTO.idConstraint.id")));
    }

    @Test
    public void updateUserRoles_CorrectUserRoles_RolesUpdated() throws Exception {
        UserUpdateRolesDTO updateRolesDTO = new UserUpdateRolesDTO();

        updateRolesDTO.setUser(getUser2());
        updateRolesDTO.getUserRoles().add(getRoleAdmin());

        updateUserRoles(updateRolesDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.roles.updated")));
    }

    @Test
    public void updateUserRoles_NotExistentUserRoles_NotFoundReturned() throws Exception {
        UserUpdateRolesDTO updateRolesDTO = new UserUpdateRolesDTO();

        updateRolesDTO.setUser(getUser2());
        updateRolesDTO.getUserRoles().add(getNotExistentRole());

        updateUserRoles(updateRolesDTO)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.role.notFound")));
    }

    @Test
    public void updateUserRoles_UserRolesNull_BadRequestReturned() throws Exception {
        UserUpdateRolesDTO updateRolesDTO = new UserUpdateRolesDTO();

        updateRolesDTO.setUser(getUser2());
        updateRolesDTO.setUserRoles(null);

        updateUserRoles(updateRolesDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is("NotEmpty.userUpdateRolesDTO.userRoles")));
    }

    @Test
    public void deleteUser_ValidUserDeleteDTO_UserDeleted() throws Exception {
        UserDeleteDTO userDeleteDTO = new UserDeleteDTO();

        userDeleteDTO.setUser(getUser2());

        deleteUser(userDeleteDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.account.deleted")));
    }

    @Test
    public void deleteUser_NotExistentUserID_NotFoundReturned() throws Exception {
        UserDeleteDTO userDeleteDTO = new UserDeleteDTO();

        userDeleteDTO.setUser(createNonExistentUser());

        deleteUser(userDeleteDTO)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.lookup.notFound")));
    }

    @Test
    public void deleteUser_SystemAdministratorID_BadRequestReturned() throws Exception {
        UserDeleteDTO deleteDTO = new UserDeleteDTO();

        deleteDTO.setUser(getUserAdmin());

        deleteUser(deleteDTO)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath(MESSAGE_CODE, is("action.notPermitted")));
    }

    @Test
    public void deleteUser_NullUserId_BadRequestReturned() throws Exception {
        UserDeleteDTO deleteDTO = new UserDeleteDTO();

        deleteDTO.setUser(getUser2());
        deleteDTO.setId(null);

        deleteUser(deleteDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is("NotNull.userDeleteDTO.idConstraint.id")));
    }

    @Test
    public void resetUserPassword_CorrectUserResetPasswordDTO_PasswordChanged() throws Exception {
        UserResetPasswordDTO resetPasswordDTO = new UserResetPasswordDTO();

        resetPasswordDTO.setUser(getUser2());
        resetPasswordDTO.setPassword("NewPassw0rd@#");

        resetUserPassword(resetPasswordDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.reset.password.success")));
    }

    @Test
    public void resetUserPassword_NotExistentID_NotFoundReturned() throws Exception {
        UserResetPasswordDTO resetPasswordDTO = new UserResetPasswordDTO();

        resetPasswordDTO.setId(null);
        resetPasswordDTO.setPassword("NewPassw0rd@#");

        resetUserPassword(resetPasswordDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is("NotNull.userResetPasswordDTO.idConstraint.id")));
    }

    @Test
    public void enableUserAccount_ParameterTrue_AccountEnabled() throws Exception {
        UserEnableDTO enableDTO = new UserEnableDTO();

        enableDTO.setUser(getUser2());
        enableDTO.setEnabled(true);

        enableUserAccount(enableDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.account.enabled")));
    }

    @Test
    public void enableUserAccount_ParameterFalse_AccountDisabled() throws Exception {
        UserEnableDTO enableDTO = new UserEnableDTO();

        enableDTO.setUser(getUser2());
        enableDTO.setEnabled(false);

        enableUserAccount(enableDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.account.disabled")));
    }

    @Test
    public void enableUserAccount_NotExistentUserID_UserNotFoundReturned() throws Exception {
        UserEnableDTO enableDTO = new UserEnableDTO();

        enableDTO.setUser(createNonExistentUser());
        enableDTO.setEnabled(true);

        enableUserAccount(enableDTO)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.lookup.notFound")));
    }

    @Test
    public void enableUserAccount_SystemAdministratorID_BadRequestReturned() throws Exception {
        UserEnableDTO enableDTO = new UserEnableDTO();

        enableDTO.setUser(getUserAdmin());
        enableDTO.setEnabled(true);

        enableUserAccount(enableDTO)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath(MESSAGE_CODE, is("action.notPermitted")));
    }

    @Test
    public void enableUserAccount_NullIDAndNullEnabled_BadRequestReturned() throws Exception {
        UserEnableDTO enableDTO = new UserEnableDTO();

        enableDTO.setId(null);
        enableDTO.setEnabled(null);

        enableUserAccount(enableDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is("NotNull.userEnableDTO.idConstraint.id")));
    }

    @Test
    public void enableUserAccount_NullEnabled_BadRequestReturned() throws Exception {
        UserEnableDTO enableDTO = new UserEnableDTO();

        enableDTO.setUser(getUser2());
        enableDTO.setEnabled(null);

        enableUserAccount(enableDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is("NotNull.userEnableDTO.enableConstraint.enabled")));
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

        updateDTO.setUser(getUser2());
        return updateDTO;
    }
}
