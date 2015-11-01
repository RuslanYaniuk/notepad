/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
(function (define) {
    "use strict";

    var REGISTER_USER_URL = 'api/registration/register-new-user';

    define(function () {
        var UserRegistrationService = function ($http) {

            var onRegisterUser = function (user) {
                return $http.put(REGISTER_USER_URL, user);
            };

            return {
                registerUser: onRegisterUser
            }
        };

        return ["$http", UserRegistrationService]
    });

})(define);