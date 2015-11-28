package com.mynote.test.unit.security;

import com.mynote.config.web.ApplicationProperties;
import com.mynote.dto.CsrfTokenDTO;
import com.mynote.dto.user.UserRegistrationDTO;
import com.mynote.test.unit.controllers.AbstractSecuredControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Duration;
import java.time.LocalTime;

import static com.mynote.config.web.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static com.mynote.test.utils.UserDtoTestUtil.createSimpleUserRegistrationDTO;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
public class BruteForceProtectionTests extends AbstractSecuredControllerTest {

    @Autowired
    private ApplicationProperties appProperties;

    @Before
    @Override
    public void setup() throws Exception {
        super.setup();

        dbUnitHelper.deleteUsersFromDb();
        dbUnitHelper.cleanInsertUsersIntoDb();
    }

    @Test
    public void whenUserRegisterAccountSuccessResponseReturnsWithPredefinedDelay() throws Exception {
        UserRegistrationDTO userDTO = createSimpleUserRegistrationDTO();
        LocalTime startTime = LocalTime.now();
        Long delay = appProperties.getBruteForceDelay();

        appProperties.setBruteForceProtectionEnabled(true);

        registerNewUser(csrfTokenDTO, userDTO).andExpect(status().isOk());

        Long duration = Duration.between(startTime, LocalTime.now()).toMillis();

        assertThat(duration, greaterThanOrEqualTo(delay));
    }

/*
    @Test
    public void whenUserLoginFailsMoreThan3TimesUserIsBlocked() throws Exception {
        UserLoginDTO user = createUserLoginDTO();

        user.setPassword("notValid");

        for (int i = 0; i < 2; i++) {
            loginUser(csrfTokenDTO, user).andExpect(status().isUnauthorized());
        }
        loginUser(csrfTokenDTO, user).andExpect(status().isForbidden());
    }
*/

    private ResultActions registerNewUser(CsrfTokenDTO csrfTokenDTO, UserRegistrationDTO userRegistrationDTO) throws Exception {
        return mockMvc.perform(put("/api/registration/register-new-user").session(session)
                        .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue())
                        .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                        .content(jacksonObjectMapper.writeValueAsString(userRegistrationDTO))
        );
    }
}
