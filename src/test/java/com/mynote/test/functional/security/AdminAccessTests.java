package com.mynote.test.functional.security;

import com.mynote.test.functional.controllers.AbstractSecuredControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@WithMockUser(username = "admin@email.com", roles = {"USER", "ADMIN"})
public class AdminAccessTests extends AbstractSecuredControllerTest {

    @Before
    public void setup() throws Exception {
        dbUnit.deleteAllFixtures();

        dbUnit.insertUsers();
        dbUnit.insertRoles();
        dbUnit.insertUsersToRoles();
    }

    @Test
    public void getIndex_AdminRole_AccessGranted() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void listAllUsers_AdminRole_AccessGranted() throws Exception {
        mockMvc.perform(get("/api/administration/list-all-users"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    public void userInfo_AdminRole_AccessGranted() throws Exception {
        mockMvc.perform(get("/api/user/get-user-info"))
                .andExpect(status().isOk());
    }
}
