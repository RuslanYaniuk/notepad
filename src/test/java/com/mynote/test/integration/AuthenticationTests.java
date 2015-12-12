package com.mynote.test.integration;

import com.mynote.config.web.Constants;
import com.mynote.dto.user.UserLoginDTO;
import com.mynote.test.unit.controllers.AbstractSecuredControllerTest;
import org.junit.Before;
import org.junit.Test;

import static com.mynote.test.utils.UserLoginDTOTestUtils.createAdminLoginDTO;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
public class AuthenticationTests extends AbstractSecuredControllerTest {

    @Before
    public void setup() throws Exception {
        dbUnitHelper.deleteUsersFromDb();
        dbUnitHelper.cleanInsertUsersIntoDb();
    }

    @Test
    public void logout_LoggedInUser_LoggedOutSuccess() throws Exception {
        UserLoginDTO admin = createAdminLoginDTO();

        loginUser(csrfTokenDTO, admin).andExpect(status().isOk()).andReturn();

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

    @Test
    public void logout_LoggedOutUser_AccessDenied() throws Exception {
        UserLoginDTO admin = createAdminLoginDTO();

        loginUser(csrfTokenDTO, admin).andExpect(status().isOk()).andReturn();

        mockMvc.perform(post("/api/auth/logout")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/administration/list-all-users")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath(MESSAGE_CODE, is(Constants.SYSTEM_MESSAGE_CODE)));
    }
}
