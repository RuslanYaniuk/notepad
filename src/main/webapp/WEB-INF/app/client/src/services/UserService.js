/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var UserService = function ($http) {

            var onGetUserInfo = function () {
                return $http.get("/api/user/get-info")
            };

            return {
                getUserInfo: onGetUserInfo
            }
        };

        return ["$http", UserService];
    });

})(define);