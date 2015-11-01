package com.mynote.test.integration.selenium;

import com.mynote.test.integration.selenium.pages.*;
import org.fluentlenium.core.annotation.Page;
import org.junit.Test;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class AdministrationTests extends AbstractFluentTest {

    @Page
    private MainPage mainPage;

    @Test
    public void admin_can_add_new_user_on_users_sub_page() {
        LoginPage loginPage = mainPage.gotoLoginPage();

        AdminPage adminPage = loginPage.loginAsAdministrator();

        AdminManageUsersPage manageUsersPage = adminPage.goToManageUsersPage();

        UserRegistrationPage registrationPage = manageUsersPage.goToAddNewUserDialog();

        registrationPage.fillFormWithCorrectDataAndSubmit();

    }
}
