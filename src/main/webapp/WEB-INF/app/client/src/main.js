/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define) {
    "use strict";

    define([
        'modules/AuthModule',
        'modules/AppModule',
        'modules/AdminModule',
        'conf/RouteManager'
    ], function (AuthModule, AppModule, AdminModule, RouteManager) {
        var appName = 'mynote',
            modules = [
                'ui.router',
                'ngMaterial',
                'angular-loading-bar',
                'ngMessages',
                'ngAnimate',
                AuthModule,
                AppModule,
                AdminModule
            ],

            app = angular.module(appName, modules)

                .config(RouteManager)

                .config(["$mdThemingProvider", function ($mdThemingProvider) {
                    $mdThemingProvider.theme('default')
                        .accentPalette('orange')
                        .primaryPalette('teal')
                        .warnPalette('red');
                }])

                .config(['$httpProvider', function ($httpProvider) {
                    $httpProvider.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';
                }])

                .config(['cfpLoadingBarProvider', function (cfpLoadingBarProvider) {
                    cfpLoadingBarProvider.includeSpinner = false;
                }]);

        angular.bootstrap(document.getElementsByTagName("body")[0], [appName]);

        return app;
    });

}(define));
