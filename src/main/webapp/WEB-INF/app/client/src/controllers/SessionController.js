/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define) {

    var LOGIN_STATE = "login";

    define(function () {

        var SessionController = function ($rootScope, $scope, $state, $http, sessionService) {

            var validateSession = function () {
                if (sessionService.isAnonymousSession()) {
                    return;
                }

                sessionService
                    .getAuthority()
                    .then(
                    function onSuccess_getAuthority(response) {
                        var userDTO = {
                            userRoleDTOs: response.data
                        };

                        sessionService.createSession(userDTO);
                    },

                    function onFault_getAuthority() {
                        sessionService.createAnonymousSession();

                        if ($state.current.name == 'index') {
                            return;
                        }
                        if ($state.current.name != LOGIN_STATE) {
                            $state.go(LOGIN_STATE);
                        }
                    });
            };

            sessionService
                .getCsrfToken(
                function onFault_getCsrfToken() {
                    //TODO print critical error
                });

            /**
             * Each time a user performs transition between states
             * it validates the session
             */
            $rootScope.$on('$stateChangeStart', validateSession);
            $rootScope.isAdminSession = sessionService.isAdminSession;
        };

        return ["$rootScope", "$scope", "$state", "$http", "sessionService", SessionController];
    })

})(define);
