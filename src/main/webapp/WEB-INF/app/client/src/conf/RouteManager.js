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
                        "menu": {templateUrl: "assets/views/index/index.menu.html"},
                        "body": {templateUrl: "assets/views/index/index.body.html"}
                    },
                    permissions: ["*"]
                })


                .state('login', {
                    url: "/login",
                    views: {
                        "menu": {templateUrl: "assets/views/index/index.menu.html"},
                        "body": {templateUrl: "assets/views/login/login.body.html"}
                    },
                    permissions: ["*"]
                })


                .state('user-registration', {
                    url: "/user-registration",
                    views: {
                        "menu": {templateUrl: "assets/views/index/index.menu.html"},
                        "body": {templateUrl: "assets/views/registration/user-registration.body.html"}
                    },
                    permissions: ["*"]
                })


                .state('admin-page', {
                    url: "/admin-page",
                    views: {
                        "menu": {templateUrl: "assets/views/index/index.menu.html"},
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
                        "menu": {templateUrl: "assets/views/index/index.menu.html"},
                        "body": {templateUrl: "assets/views/app/app.body.html"}
                    },
                    permissions: ["ROLE_USER"]
                });
        };
    });

})(define);
