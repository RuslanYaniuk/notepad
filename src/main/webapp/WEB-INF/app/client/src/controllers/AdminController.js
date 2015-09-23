/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
(function (define) {
    "use strict";

    define(function () {
        var AdminController = function ($rootScope, $scope, $state, adminService) {

            var onLoadUsers = function () {
                    $scope.loadProgress = adminService.loadUsers()
                        .then(onLoadUsersSuccess, onLoadUsersFailure);
                },

                onLoadUsersSuccess = function (response) {
                    $scope.users = response.data;
                },

                onLoadUsersFailure = function (response) {
                    //TODO handle this. I believe in you!
                };

            $scope.users = [];
            $scope.loadUsers = onLoadUsers;
        };

        return ["$rootScope", "$scope", "$state", "adminService", AdminController];
    });

})(define);
