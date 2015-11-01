package com.mynote.test.integration.selenium.pages;

import org.fluentlenium.core.annotation.AjaxElement;
import org.fluentlenium.core.annotation.Page;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.support.FindBy;

import java.util.concurrent.TimeUnit;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
public class MainPage extends AbstractFluentPage {

    @Page
    private LoginPage loginPage;

    @AjaxElement(timeOutInSeconds = 3)
    @FindBy(id = "toolbar-login-button")
    private FluentWebElement goToLoginPageButton;

    @AjaxElement(timeOutInSeconds = 3)
    @FindBy(id = "user-menu-logout-button")
    private FluentWebElement logoutButton;

    @AjaxElement(timeOutInSeconds = 3)
    @FindBy(id = "user-menu-open-button")
    private FluentWebElement menuOpenButton;

    public void hasAnonymousSession() {
        await().atMost(WAIT_SECONDS, TimeUnit.SECONDS)
                .until(element(goToLoginPageButton)).areDisplayed();

        await().atMost(WAIT_SECONDS, TimeUnit.SECONDS)
                .until(element(menuOpenButton)).areNotDisplayed();
    }

    public void hasUserSession() {
        await().atMost(WAIT_SECONDS, TimeUnit.SECONDS)
                .until(element(goToLoginPageButton)).areNotDisplayed();

        await().atMost(WAIT_SECONDS, TimeUnit.SECONDS)
                .until(element(menuOpenButton)).areDisplayed();
    }

    public LoginPage gotoLoginPage() {
        goToLoginPageButton.click();

        return loginPage;
    }

    public void logout() {
        hasUserSession();

        menuOpenButton.click();

        await().until(element(logoutButton)).areDisplayed();

        logoutButton.click();
    }
}
