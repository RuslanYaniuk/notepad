package com.mynote.test.integration;

import com.mynote.config.web.Constants;
import com.mynote.dto.user.UserLoginDTO;
import com.mynote.test.unit.controllers.AbstractSecuredControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
public class LoginLogoutTests extends AbstractSecuredControllerTest {

    @Before
    @Override
    public void setup() throws Exception {
        super.setup();

        dbUnitHelper.deleteUsersFromDb();
        dbUnitHelper.cleanInsertUsersIntoDb();
    }

    @Test
    public void logout_LoggedInUser_LoggedOutSuccess() throws Exception {
        UserLoginDTO admin = createUserLoginDTOForAdmin();

        loginUser(csrfTokenDTO, admin).andExpect(status().isOk()).andReturn();

        MvcResult result = mockMvc.perform(post("/api/logout")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isOk()).andReturn();

        checkReturnedMessageCode(result, "user.logout.success");

        mockMvc.perform(get("/api/administration/list-all-users")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void logout_LoggedOutUser_AccessDenied() throws Exception {
        UserLoginDTO admin = createUserLoginDTOForAdmin();

        loginUser(csrfTokenDTO, admin).andExpect(status().isOk()).andReturn();

        mockMvc.perform(post("/api/logout")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/api/administration/list-all-users")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isUnauthorized()).andReturn();

        checkReturnedMessageCode(result, Constants.SYSTEM_MESSAGE_CODE);
    }

    @Test
    public void logout_Anonymous_UserLoggedOut() throws Exception {
        UserLoginDTO admin = createUserLoginDTOForAdmin();

        loginUser(csrfTokenDTO, admin).andExpect(status().isOk()).andReturn();

        mockMvc.perform(get("/api/logout")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isOk()).andReturn();
    }
}
