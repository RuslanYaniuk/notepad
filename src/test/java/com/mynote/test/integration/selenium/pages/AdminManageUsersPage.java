package com.mynote.test.integration.selenium.pages;

import org.fluentlenium.core.annotation.Page;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class AdminManageUsersPage extends AbstractFluentPage {

    @Page
    private UserRegistrationPage userRegistrationPage;

    @FindBy(css = "[ng-click=\"showAddNewUserDialog()\"]")
    private FluentWebElement addNewuserDialogButton;

    public UserRegistrationPage goToAddNewUserDialog() {
        addNewuserDialogButton.click();

        return userRegistrationPage;
    }
}
