/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define) {

    define(function () {

        var SessionController = function ($rootScope,
                                          $state,
                                          sessionService,
                                          securityService) {

            var validateSession = function (event, stateTo) {
                sessionService
                    .getAuthority()
                    .then(
                    function onSuccess_getAuthority() {
                        securityService.validateNavigation(event, stateTo);
                    });
            };

            /**
             * Each time a user performs transition between states
             * it validates the session
             */
            $rootScope.$on('$stateChangeStart', validateSession);
            $rootScope.getUserEmail = sessionService.getUserEmail;
        };

        return [
            "$rootScope",
            "$state",
            "sessionService",
            "securityService", SessionController];
    })

})(define);
