package com.mynote.test.unit.security;

import com.mynote.test.unit.controllers.AbstractSecuredControllerTest;
import org.junit.Before;
import org.junit.Test;

import static com.mynote.test.utils.UserLoginDTOTestUtils.createAdminLoginDTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class AdminRoleAccessTests extends AbstractSecuredControllerTest {

    @Before
    public void setup() throws Exception {
        dbUnitHelper.deleteUsersFromDb();
        dbUnitHelper.cleanInsertUsersIntoDb();

        loginUser(csrfTokenDTO, createAdminLoginDTO());
    }

    @Test
    public void getIndex_AdminRole_AccessGranted() throws Exception {
        mockMvc.perform(get("/")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void listAllUsers_AdminRole_AccessGranted() throws Exception {
        mockMvc.perform(get("/api/administration/list-all-users")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    public void userInfo_AdminRole_AccessGranted() throws Exception {
        mockMvc.perform(get("/api/user/get-user-info")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isOk());
    }
}
