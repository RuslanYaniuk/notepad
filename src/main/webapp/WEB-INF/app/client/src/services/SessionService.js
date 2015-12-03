/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var SessionService = function ($rootScope, $http, $q, userService, userRoleService, authService) {

            var onGetAuthority = function () {

                    return authService.getAuth()
                        .then(
                        function onSuccess_getAuthority(response) {
                            var userRoles = response.data,
                                currentRoles = _session.account.userRoles;

                            if (!userRoleService.compareUserRoles(userRoles, currentRoles)) {
                                onCreateSession(userRoles);

                                if (_session.account.firstName == '' && !onIsAnonymousSession()) {
                                    userService.getUserInfo()
                                        .then(
                                        function onSuccess_getUserInfo(response) {
                                            onUpdateAccountDetails(response.data);
                                        })
                                }
                            }
                        });
                },

                onCreateSession = function (userRoles) {
                    _session.account.userRoles = userRoles;
                    _session.type = userRoleService.getHighestRole(userRoles);

                    if (onIsAdminSession() || onIsUserSession()) {
                        $rootScope.logged = true;
                    }
                },

                onClearSession = function () {
                    _session.account.firstName = '';
                    _session.account.lastName = '';
                    _session.account.email = '';
                    _session.account.userRoles = [];
                    _session.type = '';

                    $rootScope.logged = false;
                },

                onUpdateAccountDetails = function (userDTO) {
                    _session.account.firstName = userDTO.firstName;
                    _session.account.lastName = userDTO.lastName;
                    _session.account.email = userDTO.email;
                },

                onIsEmptySession = function () {
                    return _session.type == '';
                },

                onIsEmptyAccountDetails = function () {
                    return _session.account.email == '' ||
                        _session.account.firstName == '' ||
                        _session.account.lastName == '';
                },

                onIsAnonymousSession = function () {
                    return userRoleService.isAnonymous(_session.type);
                },

                onIsAdminSession = function () {
                    return userRoleService.isAdmin(_session.type);
                },

                onIsUserSession = function () {
                    return userRoleService.isUser(_session.type);
                },

                onGetUserEmail = function () {
                    return _session.account.email;
                },

                _session = {
                    account: {
                        firstName: '',
                        lastName: '',
                        email: '',
                        userRoles: []
                    },
                    type: ''
                };

            return {
                getAuthority: onGetAuthority,
                createSession: onCreateSession,
                clearSession: onClearSession,
                isEmptySession: onIsEmptySession,
                isAnonymousSession: onIsAnonymousSession,
                isUserSession: onIsUserSession,
                isAdminSession: onIsAdminSession,
                isEmptyAccountDetails: onIsEmptyAccountDetails,
                updateAccountDetails: onUpdateAccountDetails,
                getUserEmail: onGetUserEmail,
                session: _session
            }
        };

        return [
            "$rootScope",
            "$http",
            "$q",
            "userService",
            "userRoleService",
            "authService", SessionService];
    })

})(define);
