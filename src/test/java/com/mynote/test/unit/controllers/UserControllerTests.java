package com.mynote.test.unit.controllers;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static com.mynote.test.utils.UserLoginDTOTestUtils.createAdminLoginDTO;
import static com.mynote.test.utils.UserLoginDTOTestUtils.createUser2LoginDTO;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserControllerTests extends AbstractSecuredControllerTest {

    @Before
    public void setup() throws Exception {
        dbUnitHelper.deleteUsersFromDb();
        dbUnitHelper.cleanInsertUsersIntoDb();
    }

    @Test
    public void getInfo_AuthenticatedUser_UserDTOWithRolesReturned() throws Exception {
        loginUser(csrfTokenDTO, createUser2LoginDTO());

        getInfo()
                .andExpect(jsonPath("$.userRoles[0].role", not(StringUtils.EMPTY)))
                .andExpect(jsonPath("$.email", is("user2@email.com")));
    }

    @Test
    public void getInfo_UserAdmin_UserDTOWithRolesReturned() throws Exception {
        loginUser(csrfTokenDTO, createAdminLoginDTO());

        getInfo()
                .andExpect(jsonPath("$.email", is("admin@email.com")))
                .andExpect(jsonPath("$.userRoles[0].role", not(StringUtils.EMPTY)));
    }

    private ResultActions getInfo() throws Exception {
        return mockMvc.perform(get("/api/user/get-info")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isOk());
    }
}
