package com.mynote.test.functional.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.mynote.config.security.SecurityConfig;
import com.mynote.test.conf.TestApplicationConfig;
import com.mynote.test.conf.TestElasticSearchConfig;
import com.mynote.test.conf.TestJpaConfig;
import com.mynote.test.conf.TestWebConfig;
import com.mynote.test.utils.DBUnitHelper;
import com.mynote.utils.CustomMessageSource;
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
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {
        TestWebConfig.class,
        TestApplicationConfig.class,
        TestJpaConfig.class,
        TestElasticSearchConfig.class,
        SecurityConfig.class
})
@TestPropertySource("classpath:test-db.config.properties")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
public abstract class AbstractControllerTest {

    public static final String LENGTH_CODE = "Length";
    public static final String NON_SAFE_HTML_CODE = "SafeHtml";
    public static final String NOT_BLANK_CODE = "NotBlank";
    public static final String EMAIL_CODE = "Email";
    public static final String PATTERN_CODE = "Pattern";
    public static final String TAKEN_LOGIN_CODE = "TakenLogin";
    public static final String TAKEN_EMAIL_CODE = "TakenEmail";

    public static final String $_TYPE = "$.type";
    public static final String $_MESSAGE_CODE = "$.messageCode";

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    protected ObjectMapper jacksonObjectMapper;

    protected MockMvc mockMvc;

    @Autowired
    protected CustomMessageSource customMessageSource;

    @Autowired
    protected DBUnitHelper dbUnit;

    @Before
    public void buildWebAppContext() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac)
                .build();
    }

    public static String getFieldErrorCodes(String fieldName) {
        return "$.errors[?(@.field == '" + fieldName + "')].code";
    }
}
