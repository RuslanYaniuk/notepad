/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function( jasmine, requirejs ) {
        "use strict";

    var jasmineEnv       = null,
        configureJasmine = function ()
        {
            var htmlReporter = new jasmine.HtmlReporter(),
                jasmineEnv   = jasmine.getEnv(),
                filterFn     = function (spec)
                {
                    return htmlReporter.specFilter(spec);
                };

            jasmineEnv.VERBOSE        = true;
            jasmineEnv.updateInterval = 1000;
            jasmineEnv.specFilter     = filterFn;
            jasmineEnv.addReporter(htmlReporter);

            return jasmineEnv;
        };

    // Bootstrap RequireJS
    window.onload = (function( handler )
    {
        var interceptor = function()
            {
              if ( handler ) handler();
              startRequireJS();
            };

        jasmineEnv = configureJasmine( jasmine);

        return interceptor;
    })( window.onload );


    function startRequireJS ()
    {
        requirejs.config({

            baseUrl: '../src',

            paths: {

                'jquery'    : '../vendor/jquery/jquery',
                'angular'   : '../vendor/angular/angular',
                'ngRoute'      : '../vendor/angular-route/angular-route',
                'ngSanitize'   : '../vendor/angular-sanitize/angular-sanitize',

                // Special library to run AngularJS Jasmine tests with LIVE $http

                'test'          : '../test/spec/',

                // Special RequireJS plugin for "text!..." usages

                'text'      : '../vendor/_custom/require/text'
            },

            shim: {
                'angular':
                {
                    exports : 'angular'
                }
            },

            priority: 'angular'
        });


        // Manually specify all the Spec Test files...

        var dependencies = [
            'angular',
            /*'test/unit/LoginControllerSpec',*/
            'test/unit/LoginServiceSpec'
        ];


        require( dependencies , function( angular ) {

            // Prepare `test` module for all the specs (if needed)
            // Provide contextRoot for all `live` delegate testing

            angular.module('test.mynote', [ ]);

            // auto start test runner, once Require.js is done
            jasmineEnv.execute();
        });
    }

}( jasmine, requirejs ));
