package com.mynote.test.functional.security;

import com.mynote.test.functional.controllers.AbstractSecuredControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@WithMockUser(username = "user2@email.com", roles = {"USER"})
public class UserAccessTests extends AbstractSecuredControllerTest {

    @Before
    public void setup() throws Exception {
        dbUnit.deleteAllFixtures();

        dbUnit.insertUsers();
        dbUnit.insertRoles();
        dbUnit.insertUsersToRoles();
    }

    @Test
    public void userInfo_UserRole_AccessGranted() throws Exception {
        mockMvc.perform(get("/api/user/get-user-info"))
                .andExpect(status().isOk());
    }

    @Test
    public void listAllUsers_UserRole_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/administration/list-all-users"))
                .andExpect(status().isUnauthorized());
    }
}
