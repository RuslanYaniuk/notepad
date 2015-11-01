/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define) {

    define(function () {

        var SessionController = function ($rootScope,
                                          $state,
                                          $mdToast,
                                          sessionService,
                                          securityService) {

            var validateSession = function (event, stateTo) {
                    if (sessionService.isAnonymousSession()) {
                        securityService.validateNavigation(stateTo);
                        return;
                    }

                    sessionService
                        .getAuthority()
                        .then(
                        function onSuccess_getAuthority() {
                            securityService.validateNavigation(stateTo);
                        });
                };

            /**
             * Each time a user performs transition between states
             * it validates the session
             */
            $rootScope.$on('$stateChangeStart', validateSession);
            $rootScope.isAdminSession = sessionService.isAdminSession;
            $rootScope.getUserEmail = sessionService.getUserEmail;
        };

        return [
            "$rootScope",
            "$state",
            "$mdToast",
            "sessionService",
            "securityService", SessionController];
    })

})(define);
