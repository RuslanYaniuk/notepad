/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
(function (define) {
    "use strict";

    var CSRF_TOKEN_URL = "/api/login/get-token";

    define(function () {

        var CsrfService = function () {

            var onGetTokenSynchronously = function (onSuccess, onFault) {
                $.ajax({
                    method: "GET",
                    url: CSRF_TOKEN_URL,
                    async: false
                })
                    .done(onSuccess)
                    .fail(onFault);
            };

            return {
                getTokenSynchronously: onGetTokenSynchronously
            }
        };

        return [CsrfService];
    });

})(define);