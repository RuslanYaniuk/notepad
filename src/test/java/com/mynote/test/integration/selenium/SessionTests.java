package com.mynote.test.integration.selenium;

import com.mynote.test.integration.selenium.pages.AdminPage;
import com.mynote.test.integration.selenium.pages.LoginPage;
import com.mynote.test.integration.selenium.pages.MainPage;
import org.fluentlenium.core.annotation.Page;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Date;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
public class SessionTests extends AbstractFluentTest {

    @Page
    private MainPage mainPage;

    @Before
    public void setUp() {
        goTo(mainPage);
    }

    @Test
    public void when_user_is_not_logged_in_it_displays_login_button__The_anonymous_session_created() {
        mainPage.hasAnonymousSession();
    }

    @Test
    public void when_user_is_logged_in_it_shows_user_menu__The_user_session() {
        LoginPage loginPage = mainPage.gotoLoginPage();

        loginPage.loginAsUser();

        mainPage.hasUserSession();
    }

    @Test
    public void when_user_is_logged_in_and_refresh_the_page_the_session_remains__The_user_session_created() {
        LoginPage loginPage = mainPage.gotoLoginPage();

        loginPage.loginAsUser();

        goTo(mainPage);

        mainPage.hasUserSession();
    }

    @Test
    public void when_user_log_out_it_creates__The_anonymous_session_created() {
        LoginPage loginPage = mainPage.gotoLoginPage();

        loginPage.loginAsUser();

        mainPage.logout();

        mainPage.hasAnonymousSession();
    }

    /**
     * In case the backend has invalidated the user session (due to session timeout or a server reboot)
     * it should create the anonymous session and show message 'Your session has been expired'
     * after the next user navigation on app (ui_state change)
     */
    @Test
    public void when_backend_invalidates_session_and_user_navigates__The_anonymous_session_created() {
        LoginPage loginPage = mainPage.gotoLoginPage();

        AdminPage adminPage = loginPage.loginAsAdministrator();

        adminPage.isAt();

        invalidatesSession();

        adminPage.goToManageUsersPage();

        loginPage.isAt();

        /*loginPage.isToastDisplayed();*/
    }

    /**
     * In case the backend has invalidated the user session it should create the anonymous session
     * (logout the user) and show the message 'Your session has been expired'
     * after next http call with returned 401 error
     */
    @Test
    public void when_backend_invalidates_session_and_http_call__The_anonymous_session_created() {
        LoginPage loginPage = mainPage.gotoLoginPage();

        AdminPage adminPage = loginPage.loginAsAdministrator();

        adminPage.isAt();

        adminPage.goToManageUsersPage();

        invalidatesSession();

        //TODO need some functions to test...

    }


    /**
     * In case the backend has invalidated the user session after a user navigated to login page
     * it should allow the user to login without errors
     */
    @Test
    public void when_backend_invalidates_session_during_opened_login_page() {

    }

    public void invalidatesSession() {
        WebDriver.Options driverOptions = getDriver().manage();
        Cookie jsession = new Cookie("JSESSIONID", "00000000000000000000000000000000",
                null, "/", new Date(-1), false, true);
        Cookie xsrfToken = new Cookie("XSRF-TOKEN", "00000000-0000-0000-0000-000000000000",
                null, "/", new Date(-1), false, false);

        driverOptions.deleteAllCookies();

        driverOptions.addCookie(jsession);
        driverOptions.addCookie(xsrfToken);
    }
}
