package com.mynote.test.controllers;

import com.mynote.config.Constants;
import com.mynote.dto.*;
import com.mynote.services.UserRoleService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
public class LoginControllerTests extends AbstractSecuredControllerTest {

    @Before
    @Override
    public void setup() throws Exception {
        super.setup();

        dbUnitHelper.deleteUsersFromDb();
        dbUnitHelper.cleanInsertUsersIntoDb();
    }

    @Test
    public void getToken_NotAuthenticatedUsers_TokenReturned() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/login/get-token"))
                .andExpect(status().isOk()).andReturn();

        isResponseMediaTypeJson(result);

        CsrfTokenDTO csrfTokenDTO = jacksonObjectMapper.readValue(result.getResponse().getContentAsString(),
                CsrfTokenDTO.class);

        isResponseMediaTypeJson(result);

        assertNotNull(csrfTokenDTO.getHeaderName());
        assertNotNull(csrfTokenDTO.getHeaderValue());
    }

    @Test
    public void login_CorrectEmailAndPassword_LoginSuccessMessageDTOReturned() throws Exception {
        UserLoginDTO userLoginDTO = createUserLoginDTO();
        UserLoginSuccessDTO successLoginDTO;

        MvcResult result = loginUser(csrfTokenDTO, userLoginDTO).andExpect(status().isOk()).andReturn();

        isResponseMediaTypeJson(result);

        successLoginDTO = jacksonObjectMapper.readValue(result.getResponse().getContentAsString(),
                UserLoginSuccessDTO.class);

        assertNotNull(successLoginDTO.getUserDTO());

        assertThat(successLoginDTO.getUserDTO().getUserRoleDTOs()[0], is(new UserRoleDTO("ROLE_USER")));
    }

    @Test
    public void login_LoginInMixedCase_LoggedIn() throws Exception {
        UserLoginDTO userLoginDTO = createUserLoginDTO();
        UserLoginSuccessDTO successLoginDTO;

        userLoginDTO.setLogin("AndrEw95");

        MvcResult result = loginUser(csrfTokenDTO, userLoginDTO).andExpect(status().isOk()).andReturn();

        successLoginDTO = jacksonObjectMapper.readValue(result.getResponse().getContentAsString(),
                UserLoginSuccessDTO.class);

        assertThat(successLoginDTO.getMessageCode(), is("user.login.success"));
    }

    @Test
    public void login_EmailInMixedCase_LoggedIn() throws Exception {
        UserLoginDTO userLoginDTO = createUserLoginDTO();
        UserLoginSuccessDTO successLoginDTO;

        userLoginDTO.setLogin("Andrew.anderSON@mail.com");

        MvcResult result = loginUser(csrfTokenDTO, userLoginDTO).andExpect(status().isOk()).andReturn();

        successLoginDTO = jacksonObjectMapper.readValue(result.getResponse().getContentAsString(),
                UserLoginSuccessDTO.class);

        assertThat(successLoginDTO.getMessageCode(), is("user.login.success"));
    }

    @Test
    public void login_CorrectLoginAndPassword_LoggedIn() throws Exception {
        UserLoginDTO user = createUserLoginDTO();
        UserLoginSuccessDTO successLoginDTO;

        MvcResult result = loginUser(csrfTokenDTO, user).andExpect(status().isOk()).andReturn();

        successLoginDTO = jacksonObjectMapper.readValue(result.getResponse().getContentAsString(),
                UserLoginSuccessDTO.class);

        assertThat(successLoginDTO.getMessageCode(), is("user.login.success"));
    }

    @Test
    public void login_IncorrectEmail_UnauthorizedReturned() throws Exception {
        UserLoginDTO user = createUserLoginDTO();

        user.setLogin("incorrect@email");

        MvcResult result = loginUser(csrfTokenDTO, user).andExpect(status().isUnauthorized()).andReturn();
        checkReturnedMessageCode(result, "user.login.error.incorrectLoginPassword");
    }

    @Test
    public void login_EmptyCredentials_UnauthorizedReturned() throws Exception {
        UserLoginDTO user = new UserLoginDTO();

        MvcResult result = loginUser(csrfTokenDTO, user).andExpect(status().isUnauthorized()).andReturn();
        checkReturnedMessageCode(result, "user.login.validation.error.emptyCredentials");
    }

    @Test
    public void login_IncorrectPassword_UnauthorizedReturned() throws Exception {
        UserLoginDTO user = createUserLoginDTO();

        user.setPassword("notValidPassword");

        MvcResult result = loginUser(csrfTokenDTO, user).andExpect(status().isUnauthorized()).andReturn();
        checkReturnedMessageCode(result, "user.login.error.incorrectLoginPassword");
    }

    @Test
    public void login_DisabledAccount_UnauthorizedReturned() throws Exception {
        UserLoginDTO user = createDisabledUserLoginDTO();

        MvcResult result = loginUser(csrfTokenDTO, user).andExpect(status().isUnauthorized()).andReturn();
        checkReturnedMessageCode(result, "user.login.error.accountIsDisabled");
    }

    @Test
    public void login_NotValidCsrfToken_UnauthorizedReturned() throws Exception {
        UserLoginDTO user = createUserLoginDTO();

        csrfTokenDTO.setHeaderValue("not-valid-token");

        MvcResult result = loginUser(csrfTokenDTO, user).andExpect(status().isUnauthorized()).andReturn();
        checkReturnedMessageCode(result, Constants.SYSTEM_MESSAGE_CODE);
    }

    @Test
    public void authenticationStatus_AdminRole_AdminRoleReturned() throws Exception {
        loginUser(csrfTokenDTO, createUserLoginDTOForAdmin());

        MvcResult result = mockMvc.perform(get("/api/login/get-authorities")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isOk()).andReturn();

        checkRole(result, UserRoleService.ROLE_ADMIN);
    }

    @Test
    public void authenticationStatus_UserRole_UserRoleReturned() throws Exception {
        loginUser(csrfTokenDTO, createUserLoginDTO());

        MvcResult result = mockMvc.perform(get("/api/login/get-authorities")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isOk()).andReturn();

        checkRole(result, UserRoleService.ROLE_USER);
    }

    @Test
    public void getAuthentication_Anonymous_AnonymousRoleReturned() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/login/get-authorities")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isOk()).andReturn();

        checkRole(result, "ROLE_ANONYMOUS");
    }

    private void checkRole(MvcResult result, String role) throws java.io.IOException {
        UserRoleDTO[] roles = jacksonObjectMapper.readValue(result.getResponse().getContentAsString(), UserRoleDTO[].class);

        assertTrue(Arrays.asList(roles).contains(new UserRoleDTO(role)));
    }
}
