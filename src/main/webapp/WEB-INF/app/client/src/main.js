/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define) {
    "use strict";

    var dependencies = [
        'modules/AuthModule',
        'modules/AppModule',
        'modules/AdminModule',
        'conf/RouteManager'
    ];

    define(dependencies, function (AuthModule, AppModule, AdminModule, RouteManager) {
        var appName = 'mynote',
            modules = [
                'ui.router',
                'ngMaterial',
                AuthModule,
                AppModule,
                AdminModule
            ],
            app = angular.module(appName, modules).config(RouteManager);

        app.config(function ($mdThemingProvider) {
            $mdThemingProvider.theme('default').primaryPalette('teal', {
                'default': '600', // by default use shade 400 from the pink palette for primary intentions
                'hue-1': '100',   // use shade 100 for the <code>md-hue-1</code> class
                'hue-2': '600',   // use shade 600 for the <code>md-hue-2</code> class
                'hue-3': 'A100'   // use shade A100 for the <code>md-hue-3</code> class
            })
                // If you specify less than all of the keys, it will inherit from the
                // default shades
                .accentPalette('purple', {
                    'default': '200' // use shade 200 for default, and keep all other shades the same
                });
        });

        angular.bootstrap(document.getElementsByTagName("body")[0], [appName]);
        return app;
    });

}(define));
