package com.mynote.config.web;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@Component
@PropertySource("classpath:application.properties")
public class ApplicationProperties {

    public static final String PROPERTY_NAME_DATABASE_DRIVER = "db.driver";
    public static final String PROPERTY_NAME_DATABASE_PASSWORD = "db.password";
    public static final String PROPERTY_NAME_DATABASE_URL = "db.url";
    public static final String PROPERTY_NAME_DATABASE_USERNAME = "db.username";

    public static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
    public static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    public static final String PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";

    public static final String PROPERTY_NAME_HIBERNATE_EJB_NAMING_STRATEGY = "hibernate.ejb.naming_strategy";

    public static final String PROPERTY_NAME_ENTITY_MANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan";

    public static final String PROPERTY_NAME_ADMIN_EMAIL = "admin.email";
    public static final String PROPERTY_NAME_ADMIN_LOGIN = "admin.login";
    public static final String PROPERTY_NAME_ADMIN_PASSWORD = "admin.password";

    @Resource
    private Environment env;

    private String hibernateDialect;
    private String hibernateShowSql;
    private String hibernateHbm2ddlAuto;

    private String entitymanagerPackagesToScan;

    private String adminEmail;
    private String adminLogin;
    private String adminPassword;

    private Long bruteForceDelay = 1400L;
    private Boolean bruteForceProtectionEnabled = false;

    @PostConstruct
    private void init() {
        this.hibernateDialect = env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DIALECT);
        this.hibernateShowSql = env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL);
        this.hibernateHbm2ddlAuto = env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO);

        this.entitymanagerPackagesToScan = env.getRequiredProperty(PROPERTY_NAME_ENTITY_MANAGER_PACKAGES_TO_SCAN);

        this.adminEmail = env.getRequiredProperty(PROPERTY_NAME_ADMIN_EMAIL);
        this.adminLogin = env.getRequiredProperty(PROPERTY_NAME_ADMIN_LOGIN);
        this.adminPassword = env.getRequiredProperty(PROPERTY_NAME_ADMIN_PASSWORD);
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

    public String getEntityManagerPackagesToScan() {
        return entitymanagerPackagesToScan;
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

    public Boolean isBruteForceProtectionEnabled() {
        return bruteForceProtectionEnabled;
    }

    public void setBruteForceProtectionEnabled(Boolean bruteForceProtectionEnabled) {
        this.bruteForceProtectionEnabled = bruteForceProtectionEnabled;
    }

    public Long getBruteForceDelay() {
        return bruteForceDelay;
    }

    public void setBruteForceDelay(Long bruteForceDelay) {
        this.bruteForceDelay = bruteForceDelay;
    }
}
