package com.mynote.test.integration;

import com.mynote.dto.CsrfTokenDTO;
import com.mynote.dto.user.UserLoginDTO;
import com.mynote.dto.user.UserRegistrationDTO;
import com.mynote.models.User;
import com.mynote.test.unit.controllers.AbstractSecuredControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static com.mynote.config.web.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static com.mynote.test.utils.UserTestUtils.createNonExistentUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class CustomerRegistrationAndLoggingTests extends AbstractSecuredControllerTest {

    @Before
    public void setup() throws Exception {
        dbUnitHelper.deleteUsersFromDb();
        dbUnitHelper.cleanInsertUsersIntoDb();

        registerAndLogin();
    }

    @Test
    public void registeredUserCanNotAccessAdministration() throws Exception {
        mockMvc.perform(get("/api/administration/list-all-users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void registeredUserCanAccessUserInfo() throws Exception {
        mockMvc.perform(get("/api/user/get-user-info").session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isOk());
    }

    public void registerAndLogin() throws Exception {
        CsrfTokenDTO csrfTokenDTO = getCsrfToken();
        User user = createNonExistentUser();
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();

        registrationDTO.setUser(user);

        registerNewUser(csrfTokenDTO, registrationDTO).andExpect(status().isOk());

        UserLoginDTO loginDTO = new UserLoginDTO();

        loginDTO.setUser(user);

        loginUser(csrfTokenDTO, loginDTO).andExpect(status().isOk());
    }

    private ResultActions registerNewUser(CsrfTokenDTO csrfTokenDTO, UserRegistrationDTO userRegistrationDTO) throws Exception {
        return mockMvc.perform(put("/api/registration/register-new-user").session(session)
                        .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue())
                        .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                        .content(jacksonObjectMapper.writeValueAsString(userRegistrationDTO))
        );
    }
}
