package com.mynote.config.persistence;

import com.mynote.config.web.ApplicationProperties;
import org.flywaydb.core.Flyway;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Properties;

import static com.mynote.config.web.ApplicationProperties.*;

/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.mynote.repositories.jpa")
public class JpaConfig {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private DataSource dataSource;

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        Flyway flyway = new Flyway();

        flyway.setBaselineOnMigrate(true);
        flyway.setLocations("db/migration");
        flyway.setDataSource(dataSource);

        return flyway;
    }

    @Bean
    public DataSource dataSource() {
        JndiTemplate jndi = new JndiTemplate();
        DataSource dataSource;

        try {
            dataSource = (DataSource) jndi.lookup("java:/comp/env/jdbc/mynote_datasource");
        } catch (NamingException e) {
            throw new BeanCreationException("dataSource", e);
        }

        return dataSource;
    }

    @Bean
    @DependsOn("flyway")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setPackagesToScan(applicationProperties.getEntityManagerPackagesToScan());
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        entityManagerFactoryBean.setJpaProperties(hibProperties());

        return entityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public AbstractJpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

        jpaVendorAdapter.setGenerateDdl(false);
        return jpaVendorAdapter;
    }

    private Properties hibProperties() {
        Properties properties = new Properties();

        properties.put(PROPERTY_NAME_HIBERNATE_DIALECT, applicationProperties.getHibernateDialect());
        properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, applicationProperties.getHibernateShowSql());
        properties.put(PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO, applicationProperties.getHibernateHbm2ddlAuto());
        properties.put(PROPERTY_NAME_HIBERNATE_EJB_NAMING_STRATEGY, ImprovedNamingStrategy.class.getCanonicalName());

        return properties;
    }
}
