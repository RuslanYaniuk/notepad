/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define) {
    "use strict";

    var AUTH_STATUS_URL = "/api/login/get-authorities",

        SESSION_KIND_ADMIN = "admin_session",
        SESSION_KIND_USER = "customer_session",
        SESSION_KIND_ANONYMOUS = "anonymous_session";

    define(function () {

        var SessionService = function ($rootScope, $http, csrfService) {

            var onGetAuthority = function () {
                    return $http.get(AUTH_STATUS_URL);
                },

                onCreateSession = function (userDTO) {
                    var userRoles = userDTO.userRoleDTOs,
                        i;

                    for (i = 0; i < userRoles.length; i++) {
                        if (userRoles[i].role == "ROLE_ADMIN") {
                            _session.account = userDTO;
                            _session.kind = SESSION_KIND_ADMIN;
                            $rootScope.logged = true;

                            return;
                        }
                    }

                    for (i = 0; i < userRoles.length; i++) {
                        if (userRoles[i].role == "ROLE_USER") {
                            _session.kind = SESSION_KIND_USER;
                            _session.account = userDTO;
                            $rootScope.logged = true;

                            return;
                        }
                    }

                    for (i = 0; i < userRoles.length; i++) {
                        if (userRoles[i].role == "ROLE_ANONYMOUS") {
                            onCreateAnonymousSession();
                            return;
                        }
                    }
                    throw {message: "Could not parse roles"};
                },

                onCreateAnonymousSession = function () {
                    onClearSession();
                    _session.kind = SESSION_KIND_ANONYMOUS;
                },

                onClearSession = function () {
                    _session.account.firstName = '';
                    _session.account.lastName = '';
                    _session.account.emai = '';
                    _session.account.userRoles = [];
                    _session.csrfToken = '';
                    _session.kind = '';

                    $rootScope.logged = false;

                    onGetCsrfToken();
                },

                onIsEmptySession = function () {
                    return _session.kind == '';
                },

                onIsAnonymousSession = function () {
                    return _session.kind == SESSION_KIND_ANONYMOUS;
                },

                onIsAdminSession = function () {
                    return _session.kind == SESSION_KIND_ADMIN;
                },

                onGetCsrfToken = function (onFault) {
                    csrfService.getTokenSynchronously(
                        function onSuccess(response) {
                            var token = response.headerValue;

                            _session.csrfToken = token;

                            $http.defaults.headers.common = {
                                'X-CSRF-TOKEN': token
                            };
                        }, onFault);
                },

                _session = {
                    account: {
                        firstName: '',
                        lastName: '',
                        email: '',
                        userRoleDTOs: []
                    },
                    csrfToken: '',
                    kind: ''
                };

            return {
                getAuthority: onGetAuthority,
                isEmptySession: onIsEmptySession,
                isAdminSession: onIsAdminSession,
                isAnonymousSession: onIsAnonymousSession,
                clearSession: onClearSession,
                createSession: onCreateSession,
                createAnonymousSession: onCreateAnonymousSession,
                getCsrfToken: onGetCsrfToken,
                session: _session
            }
        };

        return ["$rootScope", "$http", "csrfService", SessionService];
    })

})(define);
