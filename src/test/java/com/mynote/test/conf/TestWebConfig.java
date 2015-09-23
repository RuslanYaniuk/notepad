package com.mynote.test.conf;

import com.mynote.config.WebConfig;
import com.mynote.config.db.DatabaseInitializer;
import com.mynote.config.security.SecurityConfig;
import com.mynote.test.conf.db.TestDatabaseInitializerImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@EnableWebMvc
@ComponentScan(basePackages = {"com.mynote"})
@Configuration
@Import({SecurityConfig.class})
public class TestWebConfig extends WebConfig {

    /**
     * Skip database initialization
     */
    @Override
    public DatabaseInitializer databaseInitializer() {
        return new TestDatabaseInitializerImpl();
    }
}
