/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var uiRouter = function ($stateProvider, $urlRouterProvider, $locationProvider) {

            var ROLE_ANONYMOUS = "ROLE_ANONYMOUS",
                ROLE_ADMIN = "ROLE_ADMIN",
                ROLE_USER = "ROLE_USER",
                FORBIDDEN_ALL = "FORBIDDEN_ALL",
                PERMIT_ALL = "*";

            $locationProvider.html5Mode(true);
            $urlRouterProvider.otherwise("/login");

            $stateProvider
                .state('login', {
                    url: "/login",
                    views: {
                        "tool-bar": {templateUrl: "assets/views/login/login.tool-bar.html"},
                        "body": {templateUrl: "assets/views/login/login.body.html"},
                        "footer": {templateUrl: "assets/views/index/index.footer.html"}
                    },
                    permissions: [ROLE_ANONYMOUS]
                })


                .state('user-registration', {
                    url: "/user-registration",
                    views: {
                        "tool-bar": {templateUrl: "assets/views/login/login.tool-bar.html"},
                        "body": {templateUrl: "assets/views/registration/user-registration.body.html"},
                        "footer": {templateUrl: "assets/views/index/index.footer.html"}
                    },
                    permissions: [ROLE_ANONYMOUS]
                })

                .state('application', {
                    url: "/notes",
                    views: {
                        "tool-bar": {templateUrl: "assets/views/app/app.tool-bar.html"},
                        "body": {templateUrl: "assets/views/app/app.body.html"},
                        "footer": {templateUrl: "assets/views/index/index.footer.html"},
                        "side-nav": {templateUrl: "assets/views/app/note.side-nav.html"}
                    },
                    permissions: [ROLE_USER]
                })

                // Errors
                .state('access-denied', {
                    url: "/access-denied",
                    views: {
                        "tool-bar": {templateUrl: "assets/views/login/login.tool-bar.html"},
                        "body": {templateUrl: "assets/views/errors/access-denied.body.html"},
                        "footer": {templateUrl: "assets/views/index/index.footer.html"}
                    },
                    permissions: [PERMIT_ALL]
                });
        };

        return ["$stateProvider", "$urlRouterProvider", "$locationProvider", uiRouter];
    });

})(define);
