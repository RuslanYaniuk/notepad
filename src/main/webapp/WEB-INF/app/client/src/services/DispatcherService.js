/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var DispatcherService = function ($state) {

            var UI_STATE_ADMIN_PAGE = "admin-page",
                UI_STATE_APPLICATION = "application",
                UI_STATE_INDEX = "index",
                UI_STATE_LOGIN = "login",
                UI_STATE_ACCESS_DENIED_ERROR = "access-denied";

            var onGoToLoginPage = function () {
                    $state.go(UI_STATE_LOGIN);
                },

                onGoToApplicationPage = function (options) {
                    $state.go(UI_STATE_APPLICATION, null, options);
                },

                onGoToIndexPage = function () {
                    $state.go(UI_STATE_INDEX);
                },

                onGoToAccessDeniedErrorPage = function () {
                    $state.go(UI_STATE_ACCESS_DENIED_ERROR);
                },

                onValidateUserTransition = function (event, stateTo) {
                    if ((stateTo.name == UI_STATE_LOGIN) ||
                        (stateTo.name == UI_STATE_INDEX)) {
                        onGoToApplicationPage();
                        return;
                    }

                    $state.go(stateTo.name);
                },

                onGo = function (stateTo) {
                    $state.go(stateTo.name);
                };

            return {
                goToLoginPage: onGoToLoginPage,
                goToApplicationPage: onGoToApplicationPage,
                goToIndexPage: onGoToIndexPage,
                goToAccessDeniedErrorPage: onGoToAccessDeniedErrorPage,
                validateUserTransition: onValidateUserTransition,
                go: onGo
            }
        };

        return ["$state", DispatcherService];
    });

})(define);