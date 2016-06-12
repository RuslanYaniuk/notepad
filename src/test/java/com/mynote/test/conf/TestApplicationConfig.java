package com.mynote.test.conf;

import com.mynote.config.ApplicationConfig;
import com.mynote.test.utils.TestSessionContext;
import com.mynote.utils.CustomSessionContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Ruslan Yaniuk
 * @date June 2016
 */
@Configuration
@ActiveProfiles("test")
@ComponentScan("com.mynote.test.utils")
public class TestApplicationConfig extends ApplicationConfig {

    private TestSessionContext testSessionContext = new TestSessionContext(super.customSessionContext());

    @Override
    public CustomSessionContext customSessionContext() {
        return testSessionContext;
    }

    public TestSessionContext getTestSessionContext() {
        return testSessionContext;
    }
}
