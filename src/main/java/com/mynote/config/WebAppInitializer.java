package com.mynote.config;

import com.mynote.config.persistence.ElasticsearchConfig;
import com.mynote.config.persistence.JpaConfig;
import com.mynote.config.security.SecurityConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * @author Ruslan Yaniuk
 * @date Jun 2015
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{
                ApplicationConfig.class,
                SecurityConfig.class,
                JpaConfig.class,
                ElasticsearchConfig.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{
                WebConfig.class
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
