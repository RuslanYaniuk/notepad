package com.mynote.test.controllers;

import com.mynote.dto.*;
import com.mynote.test.utils.UserDtoUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.mynote.config.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@Component
public class AdministrationControllerTests extends AbstractControllerTest implements UserJsonFixtures {

    @Before
    @Override
    public void setup() throws Exception {
        super.setup();

        dbUnitHelper.deleteUsersFromDb();
        dbUnitHelper.cleanInsertUsersIntoDb();
    }

    @Test
    public void listAllUsers_AllUsersListReturned() throws Exception {
        String allUsers = listAllUsersAsJson();

        assertThat(allUsers, is(ALL_USERS_IN_DB));
    }

    @Test
    public void updateUser_FirstNameChanged_UserUpdated() throws Exception {
        List<UserUpdateDTO> allUsersBeforeUpdate = UserDtoUtil.convertToUpdateDTO(listAllUsers());
        UserUpdateDTO user = allUsersBeforeUpdate.get(1);

        user.setFirstName("updated first Name");

        MvcResult result = updateUser(user).andExpect(status().isOk()).andReturn();

        String updatedUser = result.getResponse().getContentAsString();
        assertThat(updatedUser, is(UPDATED_FIRST_NAME_USER_ID2));

        allUsersBeforeUpdate = UserDtoUtil.convertToUpdateDTO(listAllUsers());
        user = allUsersBeforeUpdate.get(1);

        assertThat(user.getFirstName(), is("updated first Name"));
    }

    @Test
    public void updateUser_CorrectUserRoles_RolesUpdated() throws Exception {
        List<UserUpdateDTO> allUsers = UserDtoUtil.convertToUpdateDTO(listAllUsers());
        UserUpdateDTO user = allUsers.get(1);

        user.setUserRoleDTOs(getAllUserRoleDTOs());
        MvcResult result = updateUser(user).andExpect(status().isOk()).andReturn();

        String userWithUpdatedRoles = result.getResponse().getContentAsString();
        assertThat(userWithUpdatedRoles, is(UPDATED_ROLES_USER_ID2));
    }

    @Test
    public void updateUser_NotExistentUserRoles_NotFoundReturned() throws Exception {
        UserRoleDTO[] newRoles = new UserRoleDTO[2];
        List<UserUpdateDTO> allUsers = UserDtoUtil.convertToUpdateDTO(listAllUsers());
        UserUpdateDTO user = allUsers.get(1);

        newRoles[0] = new UserRoleDTO(1L, "ROLE_USER");
        newRoles[1] = new UserRoleDTO(-456L, "ROLE_NOT_EXISTENT");

        user.setUserRoleDTOs(newRoles);

        MvcResult result = updateUser(user).andExpect(status().isNotFound()).andReturn();
        checkReturnedMessageCode(result, "user.role.notFound");
    }

    @Test
    public void updateUser_UserRolesNull_BadRequestReturned() throws Exception {
        List<UserUpdateDTO> allUsers = UserDtoUtil.convertToUpdateDTO(listAllUsers());
        UserUpdateDTO user = allUsers.get(1);

        user.setUserRoleDTOs(null);

        MvcResult result = updateUser(user).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "NotNull.userUpdateDTO.userRoleDTOs");
    }

    @Test
    public void updateUser_UserRolesIdNull_BadRequestReturned() throws Exception {
        List<UserUpdateDTO> allUsers = UserDtoUtil.convertToUpdateDTO(listAllUsers());
        UserUpdateDTO user = allUsers.get(1);
        UserRoleDTO[] roles = new UserRoleDTO[1];

        roles[0] = new UserRoleDTO(null, "ROLE_USER");
        user.setUserRoleDTOs(roles);

        MvcResult result = updateUser(user).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "NotNull.userUpdateDTO.userRoleDTOs[0].id");
    }

    @Test
    public void updateUser_EmptyId_BadRequestReturned() throws Exception {
        List<UserUpdateDTO> allUsers = UserDtoUtil.convertToUpdateDTO(listAllUsers());
        UserUpdateDTO user = allUsers.get(1);

        user.setId(null);
        user.setEmail("em@ail.com");

        MvcResult result = updateUser(user).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "NotNull.userUpdateDTO.id");

        user = UserDtoUtil.convertToUpdateDTO(listAllUsers()).get(1);
        assertThat(user.getEmail(), not("em@il.com"));
    }

    @Test
    public void getUserRoles_RolesReturned() throws Exception {
        String roles = getUserRolesAsString();

        assertThat(roles, is(ALL_USER_ROLES_IN_DB));
    }

    @Test
    public void deleteUser_ValidUserDeleteDTO_UserDeleted() throws Exception {
        UserDeleteDTO userDeleteDTO = new UserDeleteDTO();

        userDeleteDTO.setId(2L);
        MvcResult result = deleteUser(userDeleteDTO).andExpect(status().isOk()).andReturn();
        checkReturnedMessageCode(result, "user.account.deleted");

        assertThat(listAllUsersAsJson(), is(ALL_USERS_IN_DB_AFTER_DELETION_ID2));
    }

    @Test
    public void deleteUser_NotExistentUserID_NotFoundReturned() throws Exception {
        UserDeleteDTO userDeleteDTO = new UserDeleteDTO();

        userDeleteDTO.setId(-456L);

        MvcResult result = deleteUser(userDeleteDTO).andExpect(status().isNotFound()).andReturn();
        checkReturnedMessageCode(result, "user.lookup.notFound");

        assertThat(listAllUsersAsJson(), is(ALL_USERS_IN_DB));
    }

    @Test
    public void deleteUser_SystemAdministratorID_BadRequestReturned() throws Exception {
        UserDeleteDTO userDeleteDTO = new UserDeleteDTO();

        userDeleteDTO.setId(1L);

        MvcResult result = deleteUser(userDeleteDTO).andExpect(status().isForbidden()).andReturn();
        checkReturnedMessageCode(result, "action.notPermitted");

        assertThat(listAllUsersAsJson(), is(ALL_USERS_IN_DB));
    }

    @Test
    public void deleteUser_NullUserID_BadRequestReturned() throws Exception {
        UserDeleteDTO userDeleteDTO = new UserDeleteDTO();

        userDeleteDTO.setId(null);

        MvcResult result = deleteUser(userDeleteDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "NotNull.userDeleteDTO.id");

        assertThat(listAllUsersAsJson(), is(ALL_USERS_IN_DB));
    }

    @Test
    public void findUser_CorrectUserId_UserDTOReturned() throws Exception {
        UserFindDTO userFindDTO = new UserFindDTO();

        userFindDTO.setId(4L);

        MvcResult result = findUser(userFindDTO).andExpect(status().isOk()).andReturn();

        assertThat(result.getResponse().getContentAsString(), is(USER_DTO_ID4));
    }

    @Test
    public void findUser_NotExistentUserId_NotFoundErrorReturned() throws Exception {
        UserFindDTO userFindDTO = new UserFindDTO();

        userFindDTO.setId(-456L);

        MvcResult result = findUser(userFindDTO).andExpect(status().isNotFound()).andReturn();
        checkReturnedMessageCode(result, "user.lookup.notFound");
    }

    @Test
    public void findUser_EmptyJson_NotAcceptableErrorReturned() throws Exception {
        UserFindDTO userFindDTO = new UserFindDTO();

        MvcResult result = findUser(userFindDTO).andExpect(status().isNotAcceptable()).andReturn();
        checkReturnedMessageCode(result, "user.lookup.fieldsAreEmpty");
    }

    @Test
    public void resetUserPassword_CorrectUserResetPasswordDTO_PasswordChanged() throws Exception {
        UserResetPasswordDTO resetPasswordDTO = new UserResetPasswordDTO(4L, "NewPassw0rd@#");

        MvcResult result = resetUserPassword(resetPasswordDTO).andExpect(status().isOk()).andReturn();

        checkReturnedMessageCode(result, "user.reset.password.success");
    }

    @Test
    public void resetUserPassword_NotExistentID_NotFoundReturned() throws Exception {
        UserResetPasswordDTO resetPasswordDTO = new UserResetPasswordDTO(-456L, "NewPassw0rd@!");

        MvcResult result = resetUserPassword(resetPasswordDTO).andExpect(status().isNotFound()).andReturn();

        checkReturnedMessageCode(result, "user.lookup.notFound");
    }

    @Test
    public void enableUserAccount_ParameterTrue_AccountEnabled() throws Exception {
        UserEnableAccountDTO disableEnableDTO = new UserEnableAccountDTO(6L, true);

        MvcResult result = enableUserAccount(disableEnableDTO).andExpect(status().isOk()).andReturn();
        checkReturnedMessageCode(result, "user.account.enabled");
    }

    @Test
    public void enableUserAccount_ParameterFalse_AccountDisabled() throws Exception {
        UserEnableAccountDTO disableEnableDTO = new UserEnableAccountDTO(4L, false);

        MvcResult result = enableUserAccount(disableEnableDTO).andExpect(status().isOk()).andReturn();
        checkReturnedMessageCode(result, "user.account.disabled");
    }

    @Test
    public void enableUserAccount_NotExistentUserID_UserNotFoundReturned() throws Exception {
        UserEnableAccountDTO disableEnableDTO = new UserEnableAccountDTO(-456L, true);

        MvcResult result = enableUserAccount(disableEnableDTO).andExpect(status().isNotFound()).andReturn();
        checkReturnedMessageCode(result, "user.lookup.notFound");
    }

    @Test
    public void enableUserAccount_SystemAdministratorID_BadRequestReturned() throws Exception {
        UserEnableAccountDTO disableEnableDTO = new UserEnableAccountDTO(1L, true);

        MvcResult result = enableUserAccount(disableEnableDTO).andExpect(status().isForbidden()).andReturn();
        checkReturnedMessageCode(result, "action.notPermitted");
    }

    @Test
    public void enableUserAccount_NullIDAndNullEnabled_BadRequestReturned() throws Exception {
        UserEnableAccountDTO disableEnableDTO = new UserEnableAccountDTO();

        MvcResult result = enableUserAccount(disableEnableDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "NotNull.userEnableAccountDTO.id");
    }

    @Test
    public void enableUserAccount_NullEnabled_BadRequestReturned() throws Exception {
        UserEnableAccountDTO disableEnableDTO = new UserEnableAccountDTO(4L, null);

        MvcResult result = enableUserAccount(disableEnableDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "NotNull.userEnableAccountDTO.enabled");
    }

    private ResultActions enableUserAccount(UserEnableAccountDTO disableEnableDTO) throws Exception {
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

    private UserDTO[] listAllUsers() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/administration/list-all-users"))
                .andExpect(status().isOk()).andReturn();

        return jacksonObjectMapper.readValue(result.getResponse().getContentAsString(), UserDTO[].class);
    }

    private String listAllUsersAsJson() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/administration/list-all-users"))
                .andExpect(status().isOk()).andReturn();

        return result.getResponse().getContentAsString();
    }

    private UserRoleDTO[] getAllUserRoleDTOs() throws Exception {
        MvcResult result = getUserRoles();

        return jacksonObjectMapper.readValue(result.getResponse().getContentAsString(), UserRoleDTO[].class);
    }

    private String getUserRolesAsString() throws Exception {
        return getUserRoles().getResponse().getContentAsString();
    }

    private MvcResult getUserRoles() throws Exception {
        return mockMvc.perform(get("/api/administration/get-all-user-roles"))
                .andExpect(status().isOk()).andReturn();
    }

    private ResultActions findUser(UserFindDTO userFindDTO) throws Exception {
        return mockMvc.perform(post("/api/administration/find-user")
                        .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                        .content(jacksonObjectMapper.writeValueAsString(userFindDTO))
        );
    }
}
