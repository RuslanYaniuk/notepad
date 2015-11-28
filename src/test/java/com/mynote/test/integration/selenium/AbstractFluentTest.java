package com.mynote.test.integration.selenium;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.mynote.test.integration.conf.SeleniumTestCaseContext;
import com.mynote.test.utils.DBUnitHelper;
import org.dbunit.DatabaseUnitException;
import org.fluentlenium.adapter.FluentTest;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SeleniumTestCaseContext.class})
@TestPropertySource("classpath:test-db.config.properties")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
public abstract class AbstractFluentTest extends FluentTest {

    @Autowired
    private DBUnitHelper dbUnitHelper;

    @Before
    public void beforeEach() throws DatabaseUnitException, SQLException, FileNotFoundException {
        dbUnitHelper.deleteUsersFromDb();
        dbUnitHelper.cleanInsertUsersIntoDb();
    }

    @Override
    public WebDriver getDefaultDriver() {
        DesiredCapabilities capability = DesiredCapabilities.chrome();

        try {
            return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capability);
        } catch (MalformedURLException e) {
            throw new BeanCreationException("webDriver", e);
        }
    }
}
