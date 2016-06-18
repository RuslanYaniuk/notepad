package com.mynote.test.functional.security;

import com.mynote.config.Constants;
import com.mynote.dto.note.NoteCreateDTO;
import com.mynote.dto.user.UserRegistrationDTO;
import com.mynote.test.functional.controllers.AbstractSecuredControllerTest;
import org.junit.Before;
import org.junit.Test;

import static com.mynote.config.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static com.mynote.test.utils.UserTestUtils.createNonExistentUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
        dbUnit.deleteAllFixtures();

        dbUnit.insertUsers();
        dbUnit.insertRoles();
        dbUnit.insertUsersToRoles();
    }

    @Test
    public void root_Anonymous_AccessGranted() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void registerNewUser_Anonymous_RegistrationSuccess() throws Exception {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();

        userRegistrationDTO.setUser(createNonExistentUser());
        mockMvc.perform(put("/api/registration/register-new-user").with(csrf())
                .contentType(Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(userRegistrationDTO))).andExpect(status().isOk());
    }

    @Test
    public void listAllUsers_Anonymous_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/administration/list-all-users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getUserInfo_Anonymous_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/user/get-user-info"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void createNote_Anonymous_Unauthorized() throws Exception {
        NoteCreateDTO createDTO = new NoteCreateDTO();

        createDTO.setSubject("Some text");
        createDTO.setText("text...");
        mockMvc.perform(put("/api/note/create").with(csrf())
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isUnauthorized());
    }
}
