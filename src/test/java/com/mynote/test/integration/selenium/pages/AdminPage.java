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
public class AdminPage extends AbstractFluentPage {

    @Page
    private AdminManageUsersPage adminManageUsersPage;

    @AjaxElement(timeOutInSeconds = 3)
    @FindBy(css = "[ui-sref=\"admin-page.users\"]")
    private FluentWebElement usersSideMenuButton;

    @AjaxElement(timeOutInSeconds = 3)
    @FindBy(css = "[ui-view=\"admin-page.body\"]")
    private FluentWebElement adminBody;

    @AjaxElement(timeOutInSeconds = 3)
    @FindBy(id = "side-menu")
    private FluentWebElement sideMenu;

    public AdminManageUsersPage goToManageUsersPage() {
        usersSideMenuButton.click();

        return adminManageUsersPage;
    }

    @Override
    public void isAt() {
        await().atMost(WAIT_SECONDS, TimeUnit.SECONDS).until(element(adminBody)).areDisplayed();
        await().atMost(WAIT_SECONDS, TimeUnit.SECONDS).until(element(sideMenu)).areDisplayed();
    }
}
