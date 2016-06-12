package com.mynote.test.conf;

import com.mynote.config.WebConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@Configuration
@ActiveProfiles("test")
public class TestWebConfig extends WebConfig {
}
