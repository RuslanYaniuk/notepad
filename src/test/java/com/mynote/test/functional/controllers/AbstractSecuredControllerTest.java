package com.mynote.test.functional.controllers;

import com.mynote.dto.CsrfTokenDTO;
import com.mynote.dto.user.UserLoginDTO;
import org.junit.Before;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static com.mynote.config.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@TestExecutionListeners({WithSecurityContextTestExecutionListener.class})
public abstract class AbstractSecuredControllerTest extends AbstractControllerTest {

    protected CsrfTokenDTO csrfTokenDTO;

    protected MockHttpSession session = new MockHttpSession();

    @Before
    @Override
    public void buildWebAppContext() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();

        csrfTokenDTO = getCsrfToken();
    }

    protected CsrfTokenDTO getCsrfToken() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/auth/get-token"))
                .andExpect(status().isOk()).andReturn();

        session = (MockHttpSession) result.getRequest().getSession();
        return jacksonObjectMapper.readValue(result.getResponse().getContentAsString(), CsrfTokenDTO.class);
    }

    protected ResultActions loginUser(CsrfTokenDTO csrfTokenDTO, UserLoginDTO userLoginDTO) throws Exception {
        return mockMvc.perform(post("/api/auth/login")
                .session(session)
                .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue())
                .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                .content(jacksonObjectMapper.writeValueAsString(userLoginDTO)));
    }
}
