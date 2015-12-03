package com.mynote.test.unit.security;

import com.mynote.test.unit.controllers.AbstractSecuredControllerTest;
import org.junit.Before;
import org.junit.Test;

import static com.mynote.test.utils.UserLoginDTOTestUtils.createUserLoginDTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserRoleAccessTests extends AbstractSecuredControllerTest {

    @Before
    public void setup() throws Exception {
        dbUnitHelper.deleteUsersFromDb();
        dbUnitHelper.cleanInsertUsersIntoDb();

        loginUser(csrfTokenDTO, createUserLoginDTO());
    }

    @Test
    public void userInfo_UserRole_AccessGranted() throws Exception {
        mockMvc.perform(get("/api/user/get-user-info")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isOk());
    }

    @Test
    public void listAllUsers_UserRole_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/administration/list-all-users")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isUnauthorized());
    }
}
