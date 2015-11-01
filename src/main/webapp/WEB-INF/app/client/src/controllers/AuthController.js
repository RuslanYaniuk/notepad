/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define) {
    "use strict";

    var ROLE_USER = "ROLE_USER",
        ROLE_ADMIN = "ROLE_ADMIN";

    define(function () {

        var AuthController = function ($rootScope,
                                       $scope,
                                       $state,
                                       authService,
                                       sessionService) {

            var onSubmit = function () {
                    if ($scope.loginForm.$valid) {
                        sessionService.clearSession();

                        authService
                            .login($scope.login, $scope.password)
                            .then(
                            function onSuccess_login(response) {
                                sessionService.updateAccountDetails(response.data.userDTO);
                                $rootScope.$broadcast('dispatchByRoles', response.data.userDTO.userRoleDTOs);
                            },

                            function onFault_login(response) {
                                var messageCode = response.data.messageCode;

                                if (response.status == 401 &&
                                    messageCode == 'user.login.error.incorrectLoginPassword') {
                                    $scope.loginForm.userPassword.$error = {'correct-credentials': true};
                                    $scope.loginForm.userLogin.$error = {'correct-credentials': true};
                                }
                            })
                    }
                },

                onLogout = function () {
                    authService.logout()
                        .then(
                        function onSuccess_Logout() {
                            sessionService.clearSession();
                            $rootScope.$broadcast('dispatchAfterLogout');
                        });
                },

                onClearIncorrectLoginError = function () {
                    $scope.loginForm.userLogin.$error = {};
                },

                onClearIncorrectPasswordError = function () {
                    $scope.loginForm.userPassword.$error = {};
                };

            $scope.submit = onSubmit;
            $scope.logout = onLogout;
            $scope.clearIncorrectLoginError = onClearIncorrectLoginError;
            $scope.clearIncorrectPasswordError = onClearIncorrectPasswordError;
        };

        return [
            "$rootScope",
            "$scope",
            "$state",
            "authService",
            "sessionService", AuthController];
    });

})(define);
