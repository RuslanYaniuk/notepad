/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
(function (define) {
    "use strict";

    var UI_STATE_ADMIN_PAGE = "admin-page",
        UI_STATE_APPLICATION = "application",
        UI_STATE_INDEX = "index",
        UI_STATE_LOGIN = "login";

    define(function () {

        var Dispatcher = function ($rootScope, $state, userRoleService) {

            var handleDispatchByRoles = function (event, userRoles) {
                    $state.go(getStateByRoles(userRoles));
                },

                handleDispatchAfterLogout = function () {
                    $state.go(UI_STATE_LOGIN);
                },

                getStateByRoles = function (userRoles) {
                    var role = userRoleService.getHighestRole(userRoles);

                    if (userRoleService.isAdmin(role)) {
                        return UI_STATE_ADMIN_PAGE;
                    }

                    if (userRoleService.isUser(role)) {
                        return UI_STATE_APPLICATION;
                    }

                    if (userRoleService.isAnonymous(role)) {
                        return UI_STATE_INDEX;
                    }

                    console.error("Unhandled role");
                    return UI_STATE_INDEX;
                };

            $rootScope.$on('dispatchByRoles', handleDispatchByRoles);
            $rootScope.$on('dispatchAfterLogout', handleDispatchAfterLogout);
        };

        return ["$rootScope", "$state", "userRoleService", Dispatcher];
    });

})(define);