/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define) {
    "use strict";

    var ROLE_USER = "ROLE_USER",
        ROLE_ADMIN = "ROLE_ADMIN";

    define(function () {

        var AuthController = function ($scope,
                                       dispatcherService,
                                       authService,
                                       sessionService) {

            var onSubmit = function () {
                    if ($scope.loginForm.$valid) {
                        sessionService.clearSession();

                        authService
                            .login($scope.login, $scope.password)
                            .then(
                            function onSuccess_login(response) {
                                var userDTO = response.data.userDTO;

                                sessionService.updateAccountDetails(userDTO);
                                sessionService.createSession(userDTO.userRoles);
                                dispatcherService.goToLatestNotesPage({location: 'replace'});
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
                            dispatcherService.goToIndexPage();
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
            "$scope",
            "dispatcherService",
            "authService",
            "sessionService", AuthController];
    });

})(define);
