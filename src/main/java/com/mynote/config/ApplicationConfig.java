package com.mynote.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.mynote.utils.CustomMessageSource;
import com.mynote.utils.CustomSessionContext;
import com.mynote.utils.validation.CustomValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@Configuration
@ComponentScan(basePackages = {
        "com.mynote.services",
        "com.mynote.utils"
})
@PropertySource("classpath:application.properties")
public class ApplicationConfig {

    public static final String PROPERTY_NAME_DATABASE_DRIVER = "db.driver";
    public static final String PROPERTY_NAME_DATABASE_PASSWORD = "db.password";
    public static final String PROPERTY_NAME_DATABASE_URL = "db.url";
    public static final String PROPERTY_NAME_DATABASE_USERNAME = "db.username";

    public static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
    public static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    public static final String PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";

    public static final String PROPERTY_NAME_HIBERNATE_EJB_NAMING_STRATEGY = "hibernate.ejb.naming_strategy";

    public static final String PROPERTY_NAME_ADMIN_EMAIL = "admin.email";
    public static final String PROPERTY_NAME_ADMIN_LOGIN = "admin.login";
    public static final String PROPERTY_NAME_ADMIN_PASSWORD = "admin.password";

    public static final String MESSAGES_LOCATION = "messages/messages";

    @Resource
    private Environment env;

    private String hibernateDialect;
    private String hibernateShowSql;
    private String hibernateHbm2ddlAuto;

    private String adminEmail;
    private String adminLogin;
    private String adminPassword;

    @PostConstruct
    private void init() {
        this.hibernateDialect = env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DIALECT);
        this.hibernateShowSql = env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL);
        this.hibernateHbm2ddlAuto = env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO);

        this.adminEmail = env.getRequiredProperty(PROPERTY_NAME_ADMIN_EMAIL);
        this.adminLogin = env.getRequiredProperty(PROPERTY_NAME_ADMIN_LOGIN);
        this.adminPassword = env.getRequiredProperty(PROPERTY_NAME_ADMIN_PASSWORD);
    }

    @Bean
    public ObjectMapper jacksonObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JSR310Module());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        return objectMapper;
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomMessageSource customMessageSource() {
        CustomMessageSource source = new CustomMessageSource();

        source.setBasename("classpath:" + MESSAGES_LOCATION);
        source.setDefaultEncoding(Constants.APPLICATION_ENCODING);
        return source;
    }

    @Bean
    public CustomValidator customValidator() {
        return new CustomValidator();
    }

    @Bean
    public CustomSessionContext customSessionContext() {
        return new CustomSessionContext();
    }

    public String getHibernateDialect() {
        return hibernateDialect;
    }

    public String getHibernateShowSql() {
        return hibernateShowSql;
    }

    public String getHibernateHbm2ddlAuto() {
        return hibernateHbm2ddlAuto;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public String getAdminLogin() {
        return adminLogin;
    }

    public String getAdminPassword() {
        return adminPassword;
    }
}
