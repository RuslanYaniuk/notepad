package com.mynote.test.unit.conf;

import com.mynote.config.WebConfig;
import com.mynote.config.security.SecurityConfig;
import com.mynote.test.commons.TestPersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@EnableWebMvc
@ComponentScan(basePackages = {"com.mynote"})
@Configuration
@Import({SecurityConfig.class, TestPersistenceContext.class})
@ActiveProfiles("test")
public class TestWebConfig extends WebConfig {

    @Autowired
    private DataSource testDataSource;

    @Override
    public DataSource dataSource() {
        return testDataSource;
    }
}
