package com.mynote.test.functional.controllers;

import com.mynote.dto.user.UserLoginDTO;
import org.junit.Before;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.ResultActions;

import static com.mynote.config.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@TestExecutionListeners({WithSecurityContextTestExecutionListener.class})
public abstract class AbstractSecuredControllerTest extends AbstractControllerTest {

    @Before
    @Override
    public void buildWebAppContext() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();
    }

    protected ResultActions loginUser(UserLoginDTO userLoginDTO) throws Exception {
        return mockMvc.perform(post("/api/auth/login").with(csrf())
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(userLoginDTO)));
    }
}
