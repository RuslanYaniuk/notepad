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
                    }
                })


                .state('login', {
                    url: "/login",
                    views: {
                        "menu": {templateUrl: "assets/views/index/index.menu.html"},
                        "body": {templateUrl: "assets/views/login/login.body.html"}
                    }
                })


                .state('admin-page', {
                    url: "/admin-page",
                    views: {
                        "menu": {templateUrl: "assets/views/index/index.menu.html"},
                        "body": {templateUrl: "assets/views/admin/admin.body.html"},
                        "side-menu": {templateUrl: "assets/views/admin/admin.side-menu.html"}
                    }
                })
                .state('admin-page.users', {
                    url: "/users",
                    views: {
                        "admin-page.body": {templateUrl: "assets/views/admin/admin.body.users.html"}
                    }
                })
                .state('admin-page.settings', {
                    url: "/settings",
                    views: {
                        "admin-page.body": {templateUrl: "assets/views/admin/admin.body.settings.html"}
                    }
                })
                .state('admin-page.statistic', {
                    url: "/statistic",
                    views: {
                        "admin-page.body": {templateUrl: "assets/views/admin/admin.body.statistic.html"}
                    }
                })


                .state('application', {
                    url: "/app",
                    templateUrl: "assets/views/app.tpl.html"
                });
        };
    });

})(define);
