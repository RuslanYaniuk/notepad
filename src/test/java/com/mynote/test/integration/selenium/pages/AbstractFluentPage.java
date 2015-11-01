package com.mynote.test.integration.selenium.pages;

import org.fluentlenium.core.FluentPage;
import org.fluentlenium.core.domain.FluentWebElement;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
public abstract class AbstractFluentPage extends FluentPage {

    public static final int PORT = 9000;
    public static final String HOST = "http://localhost:" + PORT;

    public static final long WAIT_SECONDS = 3;

    @Override
    public String getUrl() {
        return HOST;
    }

    protected String element(FluentWebElement fluentWebElement) {
        return "#" + fluentWebElement.getId();
    }
}
