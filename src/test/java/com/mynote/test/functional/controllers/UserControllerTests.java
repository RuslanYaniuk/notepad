package com.mynote.test.functional.controllers;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserControllerTests extends AbstractSecuredControllerTest {

    @Before
    public void setup() throws Exception {
        dbUnit.deleteAllFixtures();

        dbUnit.insertUsers();
        dbUnit.insertRoles();
        dbUnit.insertUsersToRoles();
    }

    @Test
    @WithMockUser(username = "user2@email.com", roles = {"USER"})
    public void getInfo_AuthenticatedUser_UserDTOWithRolesReturned() throws Exception {
        getInfo()
                .andExpect(jsonPath("$.userRoles[0].role", not(StringUtils.EMPTY)))
                .andExpect(jsonPath("$.email", is("user2@email.com")));
    }

    @Test
    @WithMockUser(username = "admin@email.com", roles = {"USER", "ADMIN"})
    public void getInfo_UserAdmin_UserDTOWithRolesReturned() throws Exception {
        getInfo()
                .andExpect(jsonPath("$.email", is("admin@email.com")))
                .andExpect(jsonPath("$.userRoles[0].role", not(StringUtils.EMPTY)));
    }

    private ResultActions getInfo() throws Exception {
        return mockMvc.perform(get("/api/user/get-info"));
    }
}
