package com.mynote.test.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.mynote.config.ExtendedMessageSource;
import com.mynote.dto.MessageDTO;
import com.mynote.dto.UserLoginDTO;
import com.mynote.test.conf.TestPersistenceContext;
import com.mynote.test.conf.TestWebConfig;
import com.mynote.test.utils.db.DBUnitHelper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static com.mynote.config.Constants.APPLICATION_JSON_UTF8;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestPersistenceContext.class, TestWebConfig.class})
@TestPropertySource("classpath:test-db.config.properties")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
public abstract class AbstractControllerTest {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    protected ObjectMapper jacksonObjectMapper;

    protected MockMvc mockMvc;

    @Autowired
    protected ExtendedMessageSource messageSource;

    @Autowired
    protected DBUnitHelper dbUnitHelper;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac)
                .build();
    }

    protected void isResponseMediaTypeJson(MvcResult result) {
        assertThat(result.getResponse().getContentType(), is(APPLICATION_JSON_UTF8));
    }

    protected void isCommonJsonResponseDTO(MvcResult result) throws IOException {
        String response = result.getResponse().getContentAsString();
        MessageDTO jsonMessageDTO = jacksonObjectMapper.readValue(response, MessageDTO.class);

        assertNotNull(jsonMessageDTO.getMessage());
    }

    protected void checkReturnedMessageCode(MvcResult result, String messageCode) throws IOException {
        MessageDTO responseMessage = jacksonObjectMapper.readValue(result.getResponse().getContentAsString(),
                MessageDTO.class);
        String actualMessage = responseMessage.getMessageCode();

        assertThat(actualMessage, Matchers.is(messageCode));
    }

    protected UserLoginDTO createUserLoginDTOForAdmin() {
        return new UserLoginDTO("administrator", "Passw0rd");
    }

    protected UserLoginDTO createUserLoginDTO() {
        return new UserLoginDTO("andrew.anderson@mail.com", "Passw0rd");
    }

    protected UserLoginDTO createDisabledUserLoginDTO() {
        return new UserLoginDTO("antony.jones@disabled.com", "Passw0rd");
    }
}
