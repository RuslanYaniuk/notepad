package com.mynote.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
@ComponentScan(basePackages = {
        "com.mynote.config.db",
        "com.mynote.config.persistence",
        "com.mynote.config.security",
        "com.mynote.config.validation",
        "com.mynote.config.web",
        "com.mynote.controllers",
        "com.mynote.services",
        "com.mynote.utils"
})
@Configuration
public class WebConfigAdapter extends WebConfig {
}
