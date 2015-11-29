package com.mynote.config.persistence;

import com.mynote.config.web.ApplicationProperties;
import org.flywaydb.core.Flyway;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.BeanCreationException;
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
import javax.persistence.EntityManagerFactory;
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

    public static final String DB_MIGRATIONS = "db/migration";
    public static final String DATASOURCE_NAME = "mynote_datasource";

    @Bean
    public DataSource dataSource() {
        JndiTemplate jndi = new JndiTemplate();
        DataSource dataSource;

        try {
            dataSource = (DataSource) jndi.lookup("java:/comp/env/jdbc/" + DATASOURCE_NAME);
        } catch (NamingException e) {
            throw new BeanCreationException("dataSource", e);
        }

        return dataSource;
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = new Flyway();

        flyway.setBaselineOnMigrate(true);
        flyway.setLocations(DB_MIGRATIONS);
        flyway.setDataSource(dataSource);

        return flyway;
    }

    @Bean
    @DependsOn("flyway")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(ApplicationProperties applicationProperties,
                                                                       Properties hibProperties,
                                                                       DataSource dataSource,
                                                                       AbstractJpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactory.setPackagesToScan(applicationProperties.getEntityManagerPackagesToScan());
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setJpaProperties(hibProperties);

        return entityManagerFactory;
    }

    @Bean
    public AbstractJpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

        jpaVendorAdapter.setGenerateDdl(false);
        return jpaVendorAdapter;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public Properties hibProperties(ApplicationProperties applicationProperties) {
        Properties properties = new Properties();

        properties.put(PROPERTY_NAME_HIBERNATE_DIALECT, applicationProperties.getHibernateDialect());
        properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, applicationProperties.getHibernateShowSql());
        properties.put(PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO, applicationProperties.getHibernateHbm2ddlAuto());
        properties.put(PROPERTY_NAME_HIBERNATE_EJB_NAMING_STRATEGY, ImprovedNamingStrategy.class.getCanonicalName());

        return properties;
    }
}