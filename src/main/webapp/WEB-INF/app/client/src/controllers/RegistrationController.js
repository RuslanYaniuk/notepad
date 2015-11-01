/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var RegistrationController = function ($scope,
                                               $mdDialog,
                                               $mdToast,
                                               userRegistrationService) {

            var onCloseDialog = function () {
                    $mdDialog.hide();
                },

                onSubmitRegistrationForm = function () {
                    if ($scope.userRegistrationForm.$valid) {
                        userRegistrationService.registerUser($scope.user)
                            .then(
                            function onSuccess_registerUser() {
                                onCloseDialog();

                                $mdToast.show({
                                    controller: 'RegistrationController',
                                    templateUrl: 'assets/views/toasts/user-add.toast.html',
                                    hideDelay: 6000,
                                    position: "top right"
                                });
                            },

                            function onFault_registerUser(response) {
                                var messageCode = response.data.messageCode;

                                if (messageCode == "") {

                                }
                            });
                    }
                };

            $scope.cancel = onCloseDialog;
            $scope.submitRegistrationForm = onSubmitRegistrationForm;

            $scope.plans = [{
                "id" : 1,
                "title": "Free"
            }, {
                "id" : 2,
                "title": "Personal"
            }];
        };

        return [
            "$scope",
            "$mdDialog",
            "$mdToast",
            "userRegistrationService", RegistrationController];
    });

})(define);