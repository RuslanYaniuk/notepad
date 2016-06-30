/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define, angular) {
    "use strict";

    define([
            "services/AuthService",
            "services/SessionService",
            "services/UserService",
            "services/ExceptionService",
            "services/SecurityService",
            "services/UserRoleService",
            "services/UserRegistrationService",
            "controllers/AuthController",
            "controllers/SessionController",
            "controllers/RegistrationController",
            "directives/CompareTo"
        ],
        function (AuthService,
                  SessionService,
                  UserService,
                  ExceptionService,
                  SecurityService,
                  UserRoleService,
                  UserRegistrationService,
                  AuthController,
                  SessionController,
                  RegistrationController,
                  CompareTo) {

            var moduleName = "mynote.Authentication";

            angular.module(moduleName, [])
                .service("sessionService", SessionService)
                .controller("SessionController", SessionController)

                .service("authService", AuthService)
                .controller('AuthController', AuthController)

                .service("ExceptionService", ExceptionService)
                .service("userService", UserService)
                .service("securityService", SecurityService)
                .service("userRoleService", UserRoleService)
                .service("userRegistrationService", UserRegistrationService)

                .controller("RegistrationController", RegistrationController)

                .directive("compareTo", CompareTo);

            return moduleName;
        });

})(define, angular);
