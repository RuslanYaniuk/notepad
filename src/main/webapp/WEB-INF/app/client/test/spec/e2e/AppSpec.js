/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function(describe) {
    "use strict";

    describe("Application tests", function() {

        it("it should add new note when 'save' button clicked", function() {
            var email = "some@email.addr",
                password = "Passw0rd";

            browser.get("/");
            element(by.id('go-to-sign-in')).click();

            element(by.model('email')).sendKeys(email);
            element(by.model('password')).sendKeys(password);
            element(by.id('sign-in')).click();

            element(by.id('save-note-btn')).click();
            browser.wait(function() {
                return element(by.className('note-row-animation')).getCssValue("margin-top").then(function(value){
                        return value  == "0px";
                    });
            }, 3000);
        })
    })

})(describe);