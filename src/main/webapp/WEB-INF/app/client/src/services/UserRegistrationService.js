/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
(function (define) {
    "use strict";

    define(function () {
        var UserRegistrationService = function ($http) {

            var REGISTER_USER_URL = 'api/registration/simple',
                CHECK_AVAILABLE_CREDENTIALS_URL = 'api/registration/check-available';

            var onRegisterUser = function (user) {
                    return $http.put(REGISTER_USER_URL, {
                        email: user.email,
                        password: user.password,
                        signIn: true
                    });
                },

                onCheckAvailableEmail = function (email) {
                    return $http.post(CHECK_AVAILABLE_CREDENTIALS_URL, {email: email});
                };

            return {
                registerUser: onRegisterUser,
                checkAvailableEmail: onCheckAvailableEmail
            }
        };

        return ["$http", UserRegistrationService]
    });

})(define);