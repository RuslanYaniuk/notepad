package com.mynote.test.conf;

import com.mynote.config.persistence.JpaConfig;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import javax.sql.DataSource;

import static com.mynote.config.ApplicationConfig.*;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
@ActiveProfiles("test")
public class TestJpaConfig extends JpaConfig {

    @Resource
    private Environment env;

    @Override
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER));
        dataSource.setUrl(env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL));
        dataSource.setUsername(env.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME));
        dataSource.setPassword(env.getRequiredProperty(PROPERTY_NAME_DATABASE_PASSWORD));

        return dataSource;
    }
}
