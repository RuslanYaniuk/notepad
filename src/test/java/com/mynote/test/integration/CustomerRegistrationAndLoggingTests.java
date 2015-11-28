package com.mynote.test.integration;

import com.mynote.dto.CsrfTokenDTO;
import com.mynote.dto.user.UserLoginDTO;
import com.mynote.dto.user.UserRegistrationDTO;
import com.mynote.test.unit.controllers.AbstractSecuredControllerTest;
import com.mynote.test.utils.UserDtoTestUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static com.mynote.config.web.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static com.mynote.test.utils.UserDtoTestUtil.createSimpleUserRegistrationDTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class CustomerRegistrationAndLoggingTests extends AbstractSecuredControllerTest {

    @Before
    @Override
    public void setup() throws Exception {
        super.setup();

        dbUnitHelper.deleteUsersFromDb();
        dbUnitHelper.cleanInsertUsersIntoDb();
    }

    @Test
    public void userCanRegisterAndLoginIntoApplication() throws Exception {
        CsrfTokenDTO csrfTokenDTO = getCsrfToken();
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();

        registerNewUser(csrfTokenDTO, userRegistrationDTO).andExpect(status().isOk());

        UserLoginDTO userLoginDTO = UserDtoTestUtil.convertToUserLoginDTO(userRegistrationDTO);

        loginUser(csrfTokenDTO, userLoginDTO).andExpect(status().isOk());
    }

    @Test
    public void registeredUserCanNotAccessAdministration() throws Exception {
        CsrfTokenDTO csrfTokenDTO = getCsrfToken();
        UserRegistrationDTO userDTO = createSimpleUserRegistrationDTO();

        registerNewUser(csrfTokenDTO, userDTO).andExpect(status().isOk());

        UserLoginDTO userLoginDTO = UserDtoTestUtil.convertToUserLoginDTO(userDTO);

        loginUser(csrfTokenDTO, userLoginDTO).andExpect(status().isOk());

        mockMvc.perform(get("/api/administration/list-all-users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void registeredUserCanAccessUserInfo() throws Exception {
        CsrfTokenDTO csrfTokenDTO = getCsrfToken();
        UserRegistrationDTO userDTO = createSimpleUserRegistrationDTO();

        registerNewUser(csrfTokenDTO, userDTO).andExpect(status().isOk());

        UserLoginDTO userLoginDTO = UserDtoTestUtil.convertToUserLoginDTO(userDTO);

        loginUser(csrfTokenDTO, userLoginDTO).andExpect(status().isOk());

        mockMvc.perform(get("/api/user/get-user-info").session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue()))
                .andExpect(status().isOk());
    }

    private ResultActions registerNewUser(CsrfTokenDTO csrfTokenDTO, UserRegistrationDTO userRegistrationDTO) throws Exception {
        return mockMvc.perform(put("/api/registration/register-new-user").session(session)
                        .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue())
                        .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                        .content(jacksonObjectMapper.writeValueAsString(userRegistrationDTO))
        );
    }
}
