/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var SecurityService = function ($state, sessionService, userRoleService) {

            var onValidateNavigation = function (stateTo) {
                    if (!hasStatePermission(sessionService.session.type, stateTo)) {
                        $state.go("login");
                    }
                },

                hasStatePermission = function (permission, stateTo) {
                    if (stateTo.permissions[0] == "*") {
                        return true;
                    }

                    return userRoleService.containsPermission(stateTo.permissions, permission);
                };

            return {
                validateNavigation: onValidateNavigation
            }
        };

        return ["$state", "sessionService", "userRoleService", SecurityService];
    });

})(define);