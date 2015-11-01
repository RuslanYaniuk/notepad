/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define, angular) {
    "use strict";

    var dependencies = [
        "services/AuthService",
        "services/SessionService",
        "services/CsrfService",
        "services/UserService",
        "services/ExceptionService",
        "services/SecurityService",
        "services/UserRoleService",
        "services/UserRegistrationService",
        "controllers/AuthController",
        "controllers/SessionController",
        "controllers/Dispatcher",
        "controllers/RegistrationController",
        "directives/PasswordValidator"
    ];

    define(dependencies, function (AuthService,
                                   SessionService,
                                   CsrfService,
                                   UserService,
                                   ExceptionService,
                                   SecurityService,
                                   UserRoleService,
                                   UserRegistrationService,
                                   AuthController,
                                   SessionController,
                                   Dispatcher,
                                   RegistrationController,
                                   PasswordValidator) {

        var moduleName = "mynote.Authentication";

        angular.module(moduleName, [])
            .service("sessionService", SessionService)
            .controller("SessionController", SessionController)

            .service("authService", AuthService)
            .controller('AuthController', AuthController)

            .service("csrfService", CsrfService)
            .service("ExceptionService", ExceptionService)
            .service("userService", UserService)
            .service("securityService", SecurityService)
            .service("userRoleService", UserRoleService)
            .service("userRegistrationService", UserRegistrationService)

            .controller("Dispatcher", Dispatcher)
            .controller("RegistrationController", RegistrationController)

            .directive("password", PasswordValidator);

        return moduleName;
    });

})(define, angular);
