package com.mynote.test.unit.controllers;

import com.mynote.dto.user.UserDTO;
import com.mynote.dto.user.UserRoleDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserControllerTests extends AbstractSecuredControllerTest {

    @Before
    @Override
    public void setup() throws Exception {
        super.setup();

        dbUnitHelper.deleteUsersFromDb();
        dbUnitHelper.cleanInsertUsersIntoDb();
    }

    @Test
    public void userInfo_AuthenticatedUser_UserDTOWithEmptyRolesReturned() throws Exception {
        loginUser(csrfTokenDTO, createUserLoginDTO());

        MvcResult result = mockMvc.perform(get("/api/user/get-user-info")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isOk()).andReturn();

        UserDTO userInfo = jacksonObjectMapper.readValue(result.getResponse().getContentAsString(),
                UserDTO.class);

        assertThat(userInfo.getEmail(), is("user2@email.com"));
        assertNull(userInfo.getUserRoleDTOs());
    }

    @Test
    public void userInfo_UserAdmin_UserDTOWithRolesReturned() throws Exception {
        loginUser(csrfTokenDTO, createUserLoginDTOForAdmin());

        MvcResult result = mockMvc.perform(get("/api/user/get-user-info")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isOk()).andReturn();

        UserDTO userInfo = jacksonObjectMapper.readValue(result.getResponse().getContentAsString(),
                UserDTO.class);

        assertThat(userInfo.getEmail(), is("admin@email.com"));

        assertTrue(Arrays.asList(userInfo.getUserRoleDTOs()).contains(new UserRoleDTO("ROLE_ADMIN")));
    }
}
