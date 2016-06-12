package com.mynote.test.functional.controllers;

import com.mynote.config.Constants;
import com.mynote.dto.user.UserLoginDTO;
import com.mynote.models.UserRole;
import com.mynote.services.UserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static com.mynote.test.utils.UserLoginDTOTestUtils.createDisabledUserLoginDTO;
import static com.mynote.test.utils.UserLoginDTOTestUtils.createUser2LoginDTO;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
public class AuthControllerTests extends AbstractSecuredControllerTest {

    public static final String USER_LOGIN_SUCCESS_CODE = "user.login.success";

    @Before
    public void setup() throws Exception {
        dbUnit.deleteAllFixtures();

        dbUnit.insertUsers();
        dbUnit.insertRoles();
        dbUnit.insertUsersToRoles();
    }

    @Test
    public void getToken_NotAuthenticatedUsers_TokenReturned() throws Exception {
        mockMvc.perform(get("/api/auth/get-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.headerName", is("X-CSRF-TOKEN")))
                .andExpect(jsonPath("$.headerValue", not(StringUtils.EMPTY)));
    }

    @Test
    public void getAuthorities_AnonymousRequest_AnonymousRoleReturned() throws Exception {
        assertThatHasRole(getAuthorities(), Constants.ROLE_ANONYMOUS);
    }

    @Test
    @WithMockUser(username = "user2@email.com", roles = {"USER"})
    public void getAuthorities_UsersRequest_UserRoleReturned() throws Exception {
        assertThatHasRole(getAuthorities(), Constants.ROLE_USER);
    }

    @Test
    @WithMockUser(username = "admin@email.com", roles = {"USER", "ADMIN"})
    public void getAuthorities_AdminsRequest_AdminRoleReturned() throws Exception {
        assertThatHasRole(getAuthorities(), Constants.ROLE_ADMIN);
    }

    @Test
    public void login_CorrectEmailAndPassword_LoginSuccessMessageDTOReturned() throws Exception {
        UserLoginDTO userLoginDTO = createUser2LoginDTO();

        MvcResult result = loginUser(csrfTokenDTO, userLoginDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath(MESSAGE_CODE, Is.is(USER_LOGIN_SUCCESS_CODE)))
                .andExpect(jsonPath("$.userDTO.userRoles[0].role", is(UserRoleService.ROLE_USER)))
                .andExpect(jsonPath("$.userDTO.login", is(userLoginDTO.getLogin())))
                .andReturn();

        isResponseMediaTypeJson(result);
    }

    @Test
    public void login_InvalidCsrfToken_UnauthorizedReturned() throws Exception {
        UserLoginDTO userLoginDTO = createUser2LoginDTO();

        csrfTokenDTO.setHeaderValue("incorrect");

        loginUser(csrfTokenDTO, userLoginDTO)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath(MESSAGE_CODE, is("csrf.error.invalidToken")));
    }

    @Test
    public void login_LoginInMixedCase_LoggedIn() throws Exception {
        UserLoginDTO userLoginDTO = createUser2LoginDTO();

        userLoginDTO.setLogin("usER3");

        loginUser(csrfTokenDTO, userLoginDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath(MESSAGE_CODE, Is.is(USER_LOGIN_SUCCESS_CODE)));
    }

    @Test
    public void login_EmailInMixedCase_LoggedIn() throws Exception {
        UserLoginDTO userLoginDTO = new UserLoginDTO();

        userLoginDTO.setLogin("usEr2@emAil.cOM");
        userLoginDTO.setPassword("Passw0rd");

        loginUser(csrfTokenDTO, userLoginDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath(MESSAGE_CODE, Is.is(USER_LOGIN_SUCCESS_CODE)));
    }

    @Test
    public void login_CorrectLoginAndPassword_LoggedIn() throws Exception {
        UserLoginDTO user = createUser2LoginDTO();

        loginUser(csrfTokenDTO, user)
                .andExpect(status().isOk())
                .andExpect(jsonPath(MESSAGE_CODE, Is.is(USER_LOGIN_SUCCESS_CODE)));
    }

    @Test
    public void login_IncorrectEmail_UnauthorizedReturned() throws Exception {
        UserLoginDTO user = createUser2LoginDTO();

        user.setLogin("incorrect@email");

        loginUser(csrfTokenDTO, user)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.login.error.incorrectLoginPassword")));
    }

    @Test
    public void login_EmptyCredentials_UnauthorizedReturned() throws Exception {
        UserLoginDTO loginDTO = new UserLoginDTO();

        loginUser(csrfTokenDTO, loginDTO)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.login.validation.error.emptyCredentials")));
    }

    @Test
    public void login_IncorrectPassword_UnauthorizedReturned() throws Exception {
        UserLoginDTO user = createUser2LoginDTO();

        user.setPassword("notValidPassword");

        loginUser(csrfTokenDTO, user)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.login.error.incorrectLoginPassword")));
    }

    @Test
    public void login_DisabledAccount_UnauthorizedReturned() throws Exception {
        UserLoginDTO user = createDisabledUserLoginDTO();

        loginUser(csrfTokenDTO, user)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.login.error.accountIsDisabled")));
    }

    @Test
    @WithMockUser(username = "user2@email.com", roles = {"USER"})
    public void logout_LoggedInUser_LoggedOutSuccess() throws Exception {
        mockMvc.perform(post("/api/auth/logout")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isOk())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.logout.success")));

        mockMvc.perform(get("/api/administration/list-all-users")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isUnauthorized());
    }

    private MvcResult getAuthorities() throws Exception {
        return mockMvc.perform(get("/api/auth/get-authorities")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isOk()).andReturn();
    }

    private void assertThatHasRole(MvcResult result, GrantedAuthority role) throws java.io.IOException {
        String json = result.getResponse().getContentAsString();
        UserRole[] roles = jacksonObjectMapper.readValue(json, UserRole[].class);

        assertThat(roles, hasItemInArray(role));
    }
}
