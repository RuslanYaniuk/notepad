package com.mynote.test.unit.security;

import com.mynote.config.web.ApplicationProperties;
import com.mynote.dto.CsrfTokenDTO;
import com.mynote.dto.user.UserLoginDTO;
import com.mynote.dto.user.UserRegistrationDTO;
import com.mynote.test.unit.controllers.AbstractSecuredControllerTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Duration;
import java.time.LocalTime;

import static com.mynote.config.web.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static com.mynote.test.utils.UserLoginDTOTestUtils.createUserLoginDTO;
import static com.mynote.test.utils.UserTestUtils.createNonExistentUser;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
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
    public void setup() throws Exception {
        dbUnitHelper.deleteUsersFromDb();
        dbUnitHelper.cleanInsertUsersIntoDb();
    }

    @Test
    public void whenUserRegisterAccountSuccessResponseReturnsWithPredefinedDelay() throws Exception {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        Long delay = appProperties.getBruteForceDelay();
        LocalTime startTime = LocalTime.now();

        registrationDTO.setUser(createNonExistentUser());
        appProperties.setBruteForceProtectionEnabled(true);

        registerNewUser(csrfTokenDTO, registrationDTO).andExpect(status().isOk());

        Long duration = Duration.between(startTime, LocalTime.now()).toMillis();
        assertThat(duration, greaterThanOrEqualTo(delay));
    }

    @Test
    @Ignore //TODO should be implemented prior production
    public void whenUserLoginFailsMoreThan3TimesUserIsBlocked() throws Exception {
        UserLoginDTO loginDTO = createUserLoginDTO();

        loginDTO.setPassword("notValid");

        for (int i = 0; i < 2; i++) {
            loginUser(csrfTokenDTO, loginDTO).andExpect(status().isUnauthorized());
        }
        loginUser(csrfTokenDTO, loginDTO).andExpect(status().isForbidden());
    }

    private ResultActions registerNewUser(CsrfTokenDTO csrfTokenDTO, UserRegistrationDTO userRegistrationDTO) throws Exception {
        return mockMvc.perform(put("/api/registration/register-new-user").session(session)
                        .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue())
                        .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                        .content(jacksonObjectMapper.writeValueAsString(userRegistrationDTO))
        );
    }
}
