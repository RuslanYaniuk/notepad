/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define) {
    "use strict";

    define(function () {
        var LOGIN_URL = "/api/auth/login",
            LOGOUT_URL = "/api/auth/logout",

            GET_AUTH_URL = "/api/auth/get-authorities";

        var AuthService = function ($http, notes) {

            var loginUser = function (login, password) {
                    var credentials = {
                        login: login,
                        password: password
                    };

                    return onGetAuth()
                        .then(
                        function onSuccess_getAuthority() {
                            return $http.post(LOGIN_URL, credentials);
                        });
                },

                logoutUser = function () {
                    notes.data.list = [];
                    return $http.post(LOGOUT_URL);
                },

                onGetAuth = function () {
                    return $http.get(GET_AUTH_URL);
                };

            return {
                getAuth: onGetAuth,
                login: loginUser,
                logout: logoutUser
            }
        };

        return ["$http", "notes", AuthService];
    })

})(define);
