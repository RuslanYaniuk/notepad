/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define) {
    "use strict";

    var LOGIN_URL = "api/login",
        LOGOUT_URL = "api/logout";

    define(function () {
        var AuthService = function ($rootScope, $state, $http) {

            var loginUser = function (login, password) {
                    var credentials = {
                        login: login,
                        password: password
                    };

                    return $http.post(LOGIN_URL, credentials);
                },

                logoutUser = function () {
                    return $http.post(LOGOUT_URL);
                };

            return {
                login: loginUser,
                logout: logoutUser
            }
        };

        return [
            "$rootScope",
            "$state",
            "$http",
            "csrfService",
            "sessionService",
            AuthService
        ];
    })

})(define);
