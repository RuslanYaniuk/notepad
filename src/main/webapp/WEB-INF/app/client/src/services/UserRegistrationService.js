/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
(function (define) {
    "use strict";

    define(function () {
        var UserRegistrationService = function ($http) {

            var REGISTER_USER_URL = 'api/registration/register-new-user',
                CHECK_AVAILABLE_CREDENTIALS_URL = 'api/registration/check-available';

            var onRegisterUser = function (user) {
                    return $http.put(REGISTER_USER_URL, user);
                },

                onCheckAvailableLogin = function (login) {
                    return $http.post(CHECK_AVAILABLE_CREDENTIALS_URL, {login: login});
                },

                onCheckAvailableEmail = function (email) {
                    return $http.post(CHECK_AVAILABLE_CREDENTIALS_URL, {email: email});
                };

            return {
                registerUser: onRegisterUser,
                checkAvailableLogin: onCheckAvailableLogin,
                checkAvailableEmail: onCheckAvailableEmail
            }
        };

        return ["$http", UserRegistrationService]
    });

})(define);