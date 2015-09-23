package com.mynote.test.services;

import com.mynote.test.conf.TestPersistenceContext;
import com.mynote.test.conf.TestWebConfig;
import org.junit.runner.RunWith;
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
@ContextConfiguration(classes = {TestPersistenceContext.class, TestWebConfig.class})
@TestPropertySource("classpath:test-db.config.properties")
public abstract class AbstractServiceTest {
}
