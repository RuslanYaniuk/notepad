/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
(function (define) {
    "use strict";

    var LIST_ALL_USERS = "/api/administration/list-all-users";

    define(function () {
        var AdminService = function ($http) {

            var onLoadUsers = function () {
                return $http.get(LIST_ALL_USERS);
            };

            return {
                loadUsers: onLoadUsers
            }
        };
        return ["$http", AdminService];
    });

})(define);