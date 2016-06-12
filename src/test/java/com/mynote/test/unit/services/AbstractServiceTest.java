package com.mynote.test.unit.services;

import com.mynote.test.conf.TestApplicationConfig;
import com.mynote.test.conf.TestElasticSearchConfig;
import com.mynote.test.conf.TestJpaConfig;
import com.mynote.test.utils.DBUnitHelper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {
        TestApplicationConfig.class,
        TestJpaConfig.class,
        TestElasticSearchConfig.class
})
@TestPropertySource("classpath:test-db.config.properties")
public abstract class AbstractServiceTest {

    @Autowired
    protected DBUnitHelper dbUnit;
}
