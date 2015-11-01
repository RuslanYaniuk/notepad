/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
(function () {

    define(function () {

        var ExceptionService = function ($mdDialog) {

            var onHandleGlobalAppException = function () {
                    $mdDialog.show(
                        $mdDialog.alert()
                            .parent(angular.element(document.querySelector('.application-scope')))
                            .clickOutsideToClose(true)
                            .title('Application error')
                            .content('An error occurred. Please refresh the page.')
                            .ariaLabel('Alert Dialog')
                            .ok('OK')
                    );
                }

                /*interceptor = ['$rootScope', '$q', function (scope, $q) {

                    function success(response) {
                        return response;
                    }

                    function error(response) {
                        var status = response.status;

                        if (status == 401) {
                            onHandleGlobalAppException();
                            return;
                        }

                        return $q.reject(response);
                    }

                    return function (promise) {
                        return promise.then(success, error);
                    }
                }]*/;

/*            $httpProvider.responseInterceptors.push(interceptor);*/

            return {
                handleGlobalAppException: onHandleGlobalAppException
            }
        };

        return ["$mdDialog", ExceptionService];
    })

})(define);
