/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function(describe) {
    "use strict";

    describe("MyNote", function() {

        it("should open login page when a user is not logged in", function() {
            browser.get("/");
            expect(browser.getTitle()).toEqual("Welcome to MyNote");
        });

        it("should go to the application page when login success", function() {
            var email = "some@email.addr",
                password = "Passw0rd";

            browser.get("/");
            element(by.linkText('Sign in')).click();

            expect(element(by.model('email')).isDisplayed()).toBeTruthy();
            expect(element(by.model('password')).isDisplayed()).toBeTruthy();

            element(by.model('email')).sendKeys(email);
            element(by.model('password')).sendKeys(password);
            element(by.buttonText("Sign in")).click();

            expect(browser.getCurrentUrl()).toContain("app");
        })
    })

})(describe);
