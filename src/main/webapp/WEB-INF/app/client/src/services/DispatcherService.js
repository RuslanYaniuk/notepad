/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var DispatcherService = function ($state) {

            var UI_STATE_APPLICATION = "application",
                UI_STATE_LOGIN = "login",
                UI_STATE_ACCESS_DENIED_ERROR = "access-denied";

            var onGoToLoginPage = function (option) {
                    $state.go(UI_STATE_LOGIN, null, option);
                },

                onGoToAccessDeniedErrorPage = function () {
                    $state.go(UI_STATE_ACCESS_DENIED_ERROR);
                },

                onGoToApplicationPage = function (option) {
                    $state.go(UI_STATE_APPLICATION, null, option);
                },

                onGo = function (stateTo) {
                    $state.go(stateTo.name);
                };

            return {
                goToLoginPage: onGoToLoginPage,
                goToAccessDeniedErrorPage: onGoToAccessDeniedErrorPage,
                goToApplicationPage: onGoToApplicationPage,
                go: onGo
            }
        };

        return ["$state", DispatcherService];
    });

})(define);