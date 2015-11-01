package com.mynote.test.integration.selenium.pages;

import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class GlobalNotofications extends AbstractFluentPage {

    @FindBy(id = "simple-toast")
    private FluentWebElement simpleToast;

    public void isNewUserAddedToast() {

    }
}
