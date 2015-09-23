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
        "controllers/AuthController",
        "controllers/SessionController"
    ];

    define(dependencies, function (AuthService, SessionService, CsrfService, AuthController, SessionController) {
        var moduleName = "mynote.Authentication";

        angular.module(moduleName, [])
            .service("sessionService", SessionService)
            .controller("SessionController", SessionController)

            .service("authService", AuthService)
            .controller('AuthController', AuthController)

            .service("csrfService", CsrfService);

        return moduleName;
    });

})(define, angular);
