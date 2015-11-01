/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define, describe) {
    "use strict";

    var dependencies = [
        'services/AuthService'
    ];

    define(dependencies, function (AuthService) {

        describe("Tests for 'AuthService'", function () {

            var httpBackend;


            /*******************************
             * Setup
             *******************************/

            beforeEach(function () {
                module('test.mynote');
            });

            beforeEach(module(function ($provide) {
                $provide.service('authService', AuthService);
            }));

            beforeEach(inject(function ($httpBackend) {
                httpBackend = $httpBackend;
            }));


            /*******************************
             * Tests
             *******************************/

            it("authService should be defined", inject(function (authService) {
                expect(authService).toBeDefined();
            }));
        });
    });

})(define, describe);
