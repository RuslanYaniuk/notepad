/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var RegistrationController = function ($scope,
                                               $mdToast,
                                               userRegistrationService) {

            var onSubmitRegistrationForm = function () {
                    if ($scope.userRegistrationForm.$valid) {
                        userRegistrationService.registerUser($scope.user)
                            .then(
                            function onSuccess_registerUser() {
                                $scope.step2Disabled = false;
                                $scope.selectedTab = 1;
                                $scope.step1Disabled = true;
                            },

                            function onFault_registerUser(response) {
                                var messageCode = response.data.messageCode;

                                if (messageCode == "") {

                                }
                            });
                    }
                },

                onCheckAvailableLogin = function () {
                    userRegistrationService.checkAvailableLogin($scope.user.login)
                        .then(function onSuccess(response) {
                            if (response.data.messageCode == 'user.login.isNotAvailable') {
                                $scope.userRegistrationForm.userLogin.$error = {'notAvailable': true};
                            }
                            if (response.data.messageCode == 'user.login.isAvailable') {
                                $scope.userRegistrationForm.userLogin.$error = {};
                            }
                        })
                },

                onCheckAvailableEmail = function () {
                    userRegistrationService.checkAvailableEmail($scope.user.email)
                        .then(function onSuccess(response) {
                            if (response.data.messageCode == 'user.email.isNotAvailable') {
                                $scope.userRegistrationForm.userEmail.$error = {'notAvailable': true};
                            }
                            if (response.data.messageCode == 'user.email.isAvailable') {
                                $scope.userRegistrationForm.userEmail.$error = {};
                            }
                        })
                };

            $scope.submitRegistrationForm = onSubmitRegistrationForm;
            $scope.checkAvailableLogin = onCheckAvailableLogin;
            $scope.checkAvailableEmail = onCheckAvailableEmail;
            $scope.step1Disabled = false;
            $scope.step2Disabled = true;
            $scope.selectedTab = 0;
        };

        return [
            "$scope",
            "$mdToast",
            "userRegistrationService", RegistrationController];
    });

})(define);