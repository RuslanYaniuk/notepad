/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
(function (define) {
    "use strict";

    define(function () {
        var AdminController = function ($rootScope,
                                        $scope,
                                        $state,
                                        $mdDialog,
                                        adminService) {

            var onLoadUsers = function () {
                    $scope.loadProgress = adminService.loadUsers()
                        .then(onLoadUsersSuccess, onLoadUsersFailure);
                },

                onLoadUsersSuccess = function (response) {
                    $scope.users = response.data;
                },

                onLoadUsersFailure = function (response) {
                    //TODO handle this. I believe in you!
                },

                onShowRegisterUserDialog = function () {
                    $mdDialog.show({
                        templateUrl: 'assets/views/app/forms/user.registration.html'
                    });
                };

            $scope.users = [];
            $scope.loadUsers = onLoadUsers;
            $scope.showRegisterUserDialog = onShowRegisterUserDialog;
        };

        return [
            "$rootScope",
            "$scope",
            "$state",
            "$mdDialog",
            "adminService", AdminController];
    });

})(define);
