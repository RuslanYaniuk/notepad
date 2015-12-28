/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var SecurityService = function (dispatcherService, sessionService, userRoleService) {

            var onValidateNavigation = function (event, stateTo) {
                    var permission = sessionService.session.type;

                    if (permission == '') {
                        permission = 'ROLE_ANONYMOUS';
                    }

                    if (!hasPermission(permission, stateTo)) {
                        event.preventDefault();

                        if (sessionService.isAnonymousSession() ||
                            sessionService.isEmptySession()) {
                            dispatcherService.goToAccessDeniedErrorPage();
                            return;
                        }

                        if (sessionService.isUserSession()) {
                            dispatcherService.goToApplicationPage();
                        }
                    }
                },

                hasPermission = function (permission, stateTo) {
                    if (stateTo.permissions[0] == "*") {
                        return true;
                    }

                    return userRoleService.containsPermission(stateTo.permissions, permission);
                };

            return {
                validateNavigation: onValidateNavigation
            }
        };

        return ["dispatcherService", "sessionService", "userRoleService", SecurityService];
    });

})(define);