/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
(function (define) {
    "use strict";

    var CSRF_TOKEN_URL = "/api/login/get-token";

    define(function () {

        var CsrfService = function ($http) {

            var onGetToken = function () {
                return $http.get(CSRF_TOKEN_URL);
            };

            return {
                getToken: onGetToken
            }
        };

        return ["$http", CsrfService];
    });

})(define);