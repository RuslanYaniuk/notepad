/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define) {
    "use strict";

    define(function () {
        var LOGIN_URL = "/api/login",
            LOGOUT_URL = "/api/logout",

            GET_AUTH_URL = "/api/login/get-authorities";

        var AuthService = function ($http) {

            var loginUser = function (login, password) {
                    var credentials = {
                        login: login,
                        password: password
                    };

                    return $http.get(GET_AUTH_URL)
                        .then(
                        function onSuccess_getAuthority() {
                            return $http.post(LOGIN_URL, credentials);
                        });
                },

                logoutUser = function () {
                    return $http.post(LOGOUT_URL);
                };

            return {
                login: loginUser,
                logout: logoutUser
            }
        };

        return ["$http", AuthService];
    })

})(define);
