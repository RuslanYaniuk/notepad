package com.mynote.test.unit.security;

import com.mynote.config.web.Constants;
import com.mynote.dto.note.NoteCreateDTO;
import com.mynote.dto.user.UserRegistrationDTO;
import com.mynote.test.unit.controllers.AbstractSecuredControllerTest;
import org.junit.Before;
import org.junit.Test;

import static com.mynote.config.web.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static com.mynote.test.utils.UserTestUtils.createNonExistentUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class AnonymousAccessTests extends AbstractSecuredControllerTest {

    @Before
    public void setup() throws Exception {
        dbUnitHelper.deleteUsersFromDb();
        dbUnitHelper.cleanInsertUsersIntoDb();
    }

    @Test
    public void root_Anonymous_AccessGranted() throws Exception {
        mockMvc.perform(get("/")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void registerNewUser_Anonymous_RegistrationSuccess() throws Exception {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();

        userRegistrationDTO.setUser(createNonExistentUser());

        mockMvc.perform(put("/api/registration/register-new-user")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue())
                .contentType(Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(userRegistrationDTO))).andExpect(status().isOk());
    }

    @Test
    public void listAllUsers_Anonymous_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/administration/list-all-users")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getUserInfo_Anonymous_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/user/get-user-info")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void createNote_Anonymous_Unauthorized() throws Exception {
        NoteCreateDTO createDTO = new NoteCreateDTO();

        createDTO.setSubject("Some text");
        createDTO.setText("text...");

        mockMvc.perform(put("/api/note/create")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue())
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isUnauthorized());
    }
}
