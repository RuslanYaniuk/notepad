/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function(define, describe) {
    "use strict";

    var dependencies = [
        'login/LoginService'
    ];

    define(dependencies, function(LoginService) {

        describe("Tests for 'LoginService'", function() {

            var httpBackend;

            beforeEach(function() {
                module('test.mynote');

                module(function($provide){
                    $provide.service( 'loginService', LoginService);
                })
            });

            beforeEach(inject(function($rootScope, $httpBackend) {
                httpBackend = $httpBackend;
            }));

            it("shows an error span when email is incorrect", inject(function(loginService) {
                httpBackend.whenGET("http://localhost:8080/j_spring_security_check").respond({
                    data: {
                        status: "ok"
                    }
                });
                loginService.login("email@now", "password");
            }));

        });

    });

})(define, describe);
