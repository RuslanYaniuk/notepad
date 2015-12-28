/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define) {
    "use strict";

    define(function () {

        return function ($stateProvider, $urlRouterProvider, $locationProvider) {

            $locationProvider.html5Mode(true);
            $urlRouterProvider.otherwise("/");

            $stateProvider
                .state('index', {
                    url: "/",
                    views: {
                        "tool-bar": {templateUrl: "assets/views/index/index.tool-bar.html"},
                        "body": {templateUrl: "assets/views/index/index.body.html"},
                        "footer": {templateUrl: "assets/views/index/index.footer.html"}
                    },
                    permissions: ["ROLE_ANONYMOUS"]
                })


                .state('login', {
                    url: "/login",
                    views: {
                        "tool-bar": {templateUrl: "assets/views/index/index.tool-bar.html"},
                        "body": {templateUrl: "assets/views/login/login.body.html"},
                        "footer": {templateUrl: "assets/views/index/index.footer.html"}
                    },
                    permissions: ["ROLE_ANONYMOUS"]
                })


                .state('user-registration', {
                    url: "/user-registration",
                    views: {
                        "tool-bar": {templateUrl: "assets/views/index/index.tool-bar.html"},
                        "body": {templateUrl: "assets/views/registration/user-registration.body.html"},
                        "footer": {templateUrl: "assets/views/index/index.footer.html"}
                    },
                    permissions: ["ROLE_ANONYMOUS"]
                })


                .state('admin-page', {
                    url: "/admin-page",
                    views: {
                        "body": {templateUrl: "assets/views/admin/admin.body.html"},
                        "side-menu": {templateUrl: "assets/views/admin/admin.side-menu.html"}
                    },
                    permissions: ["ROLE_ADMIN"]
                })
                .state('admin-page.users', {
                    url: "/users",
                    views: {
                        "admin-page.body": {templateUrl: "assets/views/admin/admin.body.users.html"}
                    },
                    permissions: ["ROLE_ADMIN"]
                })
                .state('admin-page.settings', {
                    url: "/settings",
                    views: {
                        "admin-page.body": {templateUrl: "assets/views/admin/admin.body.settings.html"}
                    },
                    permissions: ["ROLE_ADMIN"]
                })
                .state('admin-page.statistic', {
                    url: "/statistic",
                    views: {
                        "admin-page.body": {templateUrl: "assets/views/admin/admin.body.statistic.html"}
                    },
                    permissions: ["ROLE_ADMIN"]
                })


                .state('application', {
                    url: "/app",
                    views: {
                        "tool-bar": {templateUrl: "assets/views/app/app.tool-bar.html"},
                        "body": {templateUrl: "assets/views/app/app.body.html"},
                        "footer": {templateUrl: "assets/views/index/index.footer.html"},
                        "side-nav": {templateUrl: "assets/views/app/note.side-nav.html"}
                    },
                    permissions: ["ROLE_USER"]
                })

                // Errors
                .state('access-denied', {
                    url: "/access-denied",
                    views: {
                        "tool-bar": {templateUrl: "assets/views/index/index.tool-bar.html"},
                        "body": {templateUrl: "assets/views/errors/access-denied.body.html"},
                        "footer": {templateUrl: "assets/views/index/index.footer.html"}
                    },
                    permissions: ["*"]
                });
        };
    });

})(define);
