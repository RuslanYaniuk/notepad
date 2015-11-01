package com.mynote.test.integration.selenium.pages;

import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class UserRegistrationPage extends AbstractFluentPage {

    @FindBy(css = "[ng-model=\"firstName\"]")
    private FluentWebElement firstName;

    @FindBy(css = "[ng-model=\"lastName\"]")
    private FluentWebElement lastName;

    @FindBy(css = "[ng-model=\"login\"]")
    private FluentWebElement login;

    @FindBy(css = "[ng-model=\"email\"]")
    private FluentWebElement email;

    @FindBy(css = "[ng-model=\"password\"]")
    private FluentWebElement password;

    @FindBy(css = "[ng-click\"submitRegistrationForm()\"]")
    private FluentWebElement submitButton;


    public void fillFormWithCorrectDataAndSubmit() {
        firstName.text("User1000");
        lastName.text("userLastName1000");
        login.text("U$er1000Login");
        email.text("user1000@email.com");
        password.text("Pa$$w0rd");

        submitButton.click();
    }
}
