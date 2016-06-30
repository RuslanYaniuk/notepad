/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var RegistrationController = function ($scope,
                                               $mdToast,
                                               userRegistrationService,
                                               dispatcherService) {

            var onSubmit = function () {
                    if ($scope.userRegistrationForm.$valid) {
                        userRegistrationService.registerUser($scope.user)
                            .then(
                            function onSuccess_registerUser() {
                                dispatcherService.goToApplicationPage({location: 'replace'});
                            });
                    }
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

            $scope.user = {
                email: "",
                password: "",
                confirmPassword: ""
            };

            $scope.submit = onSubmit;
            $scope.checkAvailableEmail = onCheckAvailableEmail;
        };

        return [
            "$scope",
            "$mdToast",
            "userRegistrationService",
            "dispatcherService", RegistrationController];
    });

})(define);