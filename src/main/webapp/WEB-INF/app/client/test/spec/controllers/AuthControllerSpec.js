/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define, describe) {
    "use strict";

    var dependencies = [
        'controllers/AuthController',
        'services/AuthService',
        'services/SessionService',
        'services/UserService',
        'services/UserRoleService'
    ];

    define(dependencies, function (AuthController,
                                   AuthService,
                                   SessionService,
                                   UserService,
                                   UserRoleService) {

        describe("Tests for 'AuthController'", function () {

            var _scope,
                _sessionService,
                httpBackend;


            /*******************************
             * Setup
             *******************************/

            beforeEach(function () {
                module('test.mynote');

                module(function ($provide) {
                    $provide.service('authService', AuthService);
                    $provide.service('sessionService', SessionService);
                    $provide.service('userService', UserService);
                    $provide.service('userRoleService', UserRoleService);
                });
            });

            beforeEach(inject(function ($rootScope, $injector, $controller, $httpBackend) {
                _scope = $rootScope.$new();
                _sessionService = $injector.get("sessionService");

                $controller(AuthController, {
                    $scope          : _scope,
                    sessionService  : _sessionService
                });

                httpBackend = $httpBackend;
            }));

            afterEach(function () {
                _scope = null;
            });


            /*******************************
             * Tests
             *******************************/

            it("creates user session when user logs in", inject(function ($injector) {
                var roles = [
                        {"role": "ROLE_USER"}
                    ],
                    rootScope = $injector.get('$rootScope');

                mockHttpBackend(httpBackend);
                spyOn(rootScope, '$broadcast').andCallThrough();

                _scope.loginForm = {
                    $valid: true,
                    $setPristine: function () {
                    }
                };

                _scope.login = 'login';
                _scope.password = 'Passw0rd';

                _scope.submit();

                httpBackend.flush();

                expect(rootScope.$broadcast).toHaveBeenCalledWith('dispatchByRoles', roles);

                expect(_sessionService.session.account.firstName).toBe('User');
                expect(_sessionService.session.account.lastName).toBe('User last name');
                expect(_sessionService.session.account.email).toBe('user@email.com');
            }));


            /*******************************
             * Helpers
             *******************************/

            function mockHttpBackend(httpBackend) {
                httpBackend
                    .whenPOST("/api/login").respond(function (method, url, data) {
                        var responseOK = {
                                message: "Successfully logged in",
                                userDTO: {
                                    "firstName": "User",
                                    "lastName": "User last name",
                                    "email": "user@email.com",
                                    userRoleDTOs: [
                                        {"role": "ROLE_USER"}
                                    ]
                                }
                            },

                            responseNotAuth = {
                                messageCode: 'user.login.error.incorrectLoginPassword'
                            },

                            credentials = angular.fromJson(data);

                        if (credentials.password != 'Passw0rd' && credentials.login != 'login') {
                            return [401, responseNotAuth];
                        }

                        return [200, responseOK];
                    });

                httpBackend
                    .whenPOST("/api/logout").respond(function () {
                        return [
                            200,
                            {"message": "Successfully logged in"}
                        ];
                    });

                httpBackend
                    .whenGET("/api/login/get-authorities")
                    .respond(function () {
                        //TODO mock 'Set-Cookies' for XSRF-TOKEN
                        return [200];
                    });
            }
        })
    })

})(define, describe);
