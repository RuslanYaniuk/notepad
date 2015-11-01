package com.mynote.test.integration.selenium.pages;

import org.fluentlenium.core.annotation.AjaxElement;
import org.fluentlenium.core.annotation.Page;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.support.FindBy;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
public class LoginPage extends AbstractFluentPage {

    @AjaxElement(timeOutInSeconds = 3)
    @FindBy(css = "[ng-model=\"login\"]")
    private FluentWebElement loginInput;

    @AjaxElement(timeOutInSeconds = 3)
    @FindBy(css = "[ng-model=\"password\"]")
    private FluentWebElement passwordInput;

    @AjaxElement(timeOutInSeconds = 3)
    @FindBy(css = "[ng-click=\"submit()\"]")
    private FluentWebElement loginButton;

    @AjaxElement(timeOutInSeconds = 3)
    @FindBy(id = "login-view")
    private FluentWebElement loginView;

    @FindBy(id = "user-info-toast")
    private FluentWebElement infoToast;

    @Page
    private AdminPage adminPage;

    @Override
    public String getUrl() {
        return HOST + "/login";
    }

    public void loginAsUser() {
        loginInput.text("user2");
        passwordInput.text("Passw0rd");
        loginButton.click();
    }

    public AdminPage loginAsAdministrator() {
        loginInput.text("admin");
        passwordInput.text("Passw0rd");
        loginButton.click();

        return adminPage;
    }

    public void isToastDisplayed() {
        await().atMost(WAIT_SECONDS, TimeUnit.SECONDS).until(element(infoToast)).areDisplayed();
    }

    @Override
    public void isAt() {
        assertTrue(loginView.isDisplayed());
    }
}
