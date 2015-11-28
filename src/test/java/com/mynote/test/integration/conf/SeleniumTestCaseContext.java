package com.mynote.test.integration.conf;

import com.mynote.test.conf.TestJpaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
@Configuration
@Import({TestJpaConfig.class})
@ComponentScan(basePackages = {
        "com.mynote.test.utils",
        "com.mynote.test.integration.conf",
        "com.mynote.test.integration.selenium"
})
public class SeleniumTestCaseContext {

    @Autowired
    private DataSource testDataSource;

    @Bean
    public DataSource dataSource() {
        return testDataSource;
    }
}
