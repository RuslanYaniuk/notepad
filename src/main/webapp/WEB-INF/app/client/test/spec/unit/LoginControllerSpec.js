/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function(define, describe) {
    "use strict";

    var dependencies = [
        'login/controllers/LoginController'
    ];

    define(dependencies, function(LoginController) {

        describe("Tests for 'LoginController'", function() {

            var _scope;

            beforeEach(module('test.mynote'));

            beforeEach(inject(function($rootScope, $controller) {
                _scope = $rootScope.$new();

                $controller(LoginController, {
                    $scope: _scope
                })
            }));

            it("shows an error span when email is incorrect", function() {
                _scope.email = "incorrect";
                _scope.submit();
                expect(_scope.emailError).toBe(true);

                _scope.email = "";
                _scope.submit();
                expect(_scope.emailError).toBe(true);

                _scope.email =  new Array(256);
                _scope.submit();
                expect(_scope.emailError).toBe(true);

                _scope.password = "correctpassw0rd";
                _scope.email = "correct@email.com";
                _scope.submit();
                expect(_scope.emailError).toBe(false);
            });

            it("shows an error span when password is empty or too short", function() {
                _scope.email = "correct@email.com";
                _scope.password = "";
                _scope.submit();
                expect(_scope.passwordError).toBe(true);

                _scope.password = new Array(256);
                _scope.submit();
                expect(_scope.passwordError).toBe(true);

                _scope.password = "normalpassw0rd";
                _scope.submit();
                expect(_scope.passwordError).toBe(false);
            })

        })
    })

})(define, describe);
