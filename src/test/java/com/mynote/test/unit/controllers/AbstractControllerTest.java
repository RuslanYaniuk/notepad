package com.mynote.test.unit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.mynote.config.web.ExtendedMessageSource;
import com.mynote.test.unit.conf.TestWebConfig;
import com.mynote.test.utils.DBUnitHelper;
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

import static com.mynote.config.web.Constants.APPLICATION_JSON_UTF8;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestWebConfig.class})
@TestPropertySource("classpath:test-db.config.properties")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
public abstract class AbstractControllerTest {

    public static final String MESSAGE_CODE = "$.messageCode";

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
    public void buildWebAppContext() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac)
                .build();
    }

    protected void isResponseMediaTypeJson(MvcResult result) {
        assertThat(result.getResponse().getContentType(), is(APPLICATION_JSON_UTF8));
    }
}
