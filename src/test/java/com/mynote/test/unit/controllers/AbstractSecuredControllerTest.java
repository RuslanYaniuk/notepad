package com.mynote.test.unit.controllers;

import com.mynote.dto.CsrfTokenDTO;
import com.mynote.dto.UserLoginDTO;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.Filter;

import static com.mynote.config.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public abstract class AbstractSecuredControllerTest extends AbstractControllerTest {

    protected CsrfTokenDTO csrfTokenDTO;

    protected MockHttpSession session = new MockHttpSession();

    @Autowired
    private Filter springSecurityFilterChain;

    @Before
    @Override
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac)
                .addFilters(springSecurityFilterChain)
                .build();

        csrfTokenDTO = getCsrfToken();
    }

    protected CsrfTokenDTO getCsrfToken() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/login/get-token"))
                .andExpect(status().isOk()).andReturn();

        session = (MockHttpSession)result.getRequest().getSession();

        return jacksonObjectMapper.readValue(result.getResponse().getContentAsString(), CsrfTokenDTO.class);
    }

    protected ResultActions loginUser(CsrfTokenDTO csrfTokenDTO, UserLoginDTO userLoginDTO) throws Exception {
        return mockMvc.perform(post("/api/login")
                        .session(session)
                        .header(csrfTokenDTO.getHeaderName(), csrfTokenDTO.getHeaderValue())
                        .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                        .content(jacksonObjectMapper.writeValueAsString(userLoginDTO)));
    }
}
