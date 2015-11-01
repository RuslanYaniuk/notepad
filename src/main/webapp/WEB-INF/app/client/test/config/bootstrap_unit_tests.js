/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (jasmine, requirejs) {
    "use strict";

    var jasmineEnv = null,
        configureJasmine = function () {
            var htmlReporter = new jasmine.HtmlReporter(),
                jasmineEnv = jasmine.getEnv(),
                filterFn = function (spec) {
                    return htmlReporter.specFilter(spec);
                };

            jasmineEnv.VERBOSE = true;
            jasmineEnv.updateInterval = 1000;
            jasmineEnv.specFilter = filterFn;
            jasmineEnv.addReporter(htmlReporter);

            return jasmineEnv;
        };

    // Bootstrap RequireJS
    window.onload = (function (handler) {
        var interceptor = function () {
            if (handler) handler();
            startRequireJS();
        };

        jasmineEnv = configureJasmine(jasmine);

        return interceptor;
    })(window.onload);


    function startRequireJS() {
        requirejs.config({

            baseUrl: '../src',

            paths: {

                'angular'  : '../assets/vendor/angular/angular',
                'ui.router': '../assets/vendor/angular-ui-router/angular-ui-router',

                'test'     : '../test/spec'
            },

            shim: {
                'angular': {
                    exports: 'angular'
                }
            },

            priority: 'angular'
        });


        // Manually specify all the Spec Test files...

        var dependencies = [
            'angular',
            'ui.router',
            'test/controllers/AuthControllerSpec',
            'test/services/AuthServiceSpec'
        ];


        require(dependencies, function (angular) {

            // Prepare `test` module for all the specs (if needed)
            // Provide contextRoot for all `live` delegate testing

            angular.module('test.mynote', ['ui.router']);

            // auto start test runner, once Require.js is done
            jasmineEnv.execute();
        });
    }

}(jasmine, requirejs));
