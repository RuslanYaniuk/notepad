package com.mynote.test.unit.conf;

import com.mynote.config.security.SecurityConfig;
import com.mynote.config.session.HttpSessionContext;
import com.mynote.test.conf.TestElasticSearchConfig;
import com.mynote.test.conf.TestJpaConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
@ComponentScan(basePackages = {
        "com.mynote.config.db",
        "com.mynote.config.security",
        "com.mynote.config.validation",
        "com.mynote.config.web",
        "com.mynote.controllers",
        "com.mynote.services",
        "com.mynote.utils",
        "com.mynote.test.utils"
})
@Import({
        SecurityConfig.class,
        TestJpaConfig.class,
        TestElasticSearchConfig.class
})
@Configuration
@ActiveProfiles("test")
public class MockedSessionWebConfig extends TestWebConfig {

    @Override
    public HttpSessionContext httpSessionContext() {
        return new TestHttpSessionContext();
    }
}
