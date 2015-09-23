/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define) {
    "use strict";

    var UI_STATE_ADMIN_PAGE = "admin-page",
        UI_STATE_APPLICATION = "application",
        UI_STATE_INDEX = "index",
        UI_STATE_LOGIN = "login",

        ROLE_USER = "ROLE_USER",
        ROLE_ADMIN = "ROLE_ADMIN";

    define(function () {

        var AuthController = function ($rootScope, $scope, $state, authService, sessionService, $mdDialog) {

            var onSubmit = function () {
                    if ($scope.loginForm.$valid) {
                        authService
                            .login($scope.login, $scope.password)
                            .then(
                            function onSuccess_login(response) {
                                var userDTO = response.data.userDTO;

                                sessionService.createSession(userDTO);
                                $state.go(dispatchAfterLogin(userDTO));
                            },

                            function onFault_login(response) {
                                var errorMessageContainer = $(".error-message"),
                                    errorSpan = errorMessageContainer.children("span"),
                                    message = response.data.message;

                                if (message == null || "") {
                                    message = "Some error occurred.";
                                }
                                if (response.status == 404) {
                                    message = "Could not connect to the application server.";
                                }

                                errorSpan.text(message);
                                errorMessageContainer.height((errorSpan.outerHeight() > 48
                                    ? errorSpan.outerHeight() : 48));

                                sessionService.clearSession();
                            }
                        )
                    }
                },

                dispatchAfterLogin = function (userDTO) {
                    var i,
                        userRoles = userDTO.userRoleDTOs;

                    for (i = 0; i < userRoles.length; i++) {
                        if (userRoles[i].role == ROLE_ADMIN) {
                            return UI_STATE_ADMIN_PAGE;
                        }
                    }
                    for (i = 0; i < userRoles.length; i++) {
                        if (userRoles[i].role == ROLE_USER) {
                            return UI_STATE_APPLICATION;
                        }
                    }

                    showGeneralAppError();
                },

                onLogout = function () {
                    authService.logout().then(
                        function onSuccess_logout() {
                            sessionService.createAnonymousSession();

                            if ($state.current.name != UI_STATE_LOGIN) {
                                $state.go(UI_STATE_LOGIN);
                            }
                        },
                        function onFault_logout() {
                            sessionService.createAnonymousSession();
                            showGeneralAppError();
                        });
                },

                showGeneralAppError = function () {
                    $mdDialog.show(
                        $mdDialog.alert()
                            .parent(angular.element(document.querySelector('.application-scope')))
                            .clickOutsideToClose(true)
                            .title('Application error')
                            .content('An error occurred during logout. Please refresh the page.')
                            .ariaLabel('Alert Dialog')
                            .ok('Got it!')
                    );
                };

            $scope.submit = onSubmit;
            $scope.logout = onLogout;
        };

        return [
            "$rootScope",
            "$scope",
            "$state",
            "authService",
            "sessionService",
            "$mdDialog",
            AuthController];
    });

})(define);
