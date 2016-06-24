/**
 * @author Ruslan Yaniuk
 * @date June 2016
 */
(function (define) {
    "use strict";

    define(function () {

        var OnScroll = function () {
            var barrier = 85;

            return {
                restrict: 'A',
                link: function (scope, element) {
                    element.bind('scroll', function () {
                        var target = element[0],
                            scrolledPercents = ((target.scrollTop + target.clientHeight) /
                                (target.scrollHeight / 100)) | 0;

                        if (scrolledPercents < barrier) {
                            scope.percentRiched = false;
                        }
                        if (scope.percentRiched == undefined || scope.percentRiched == false) {
                            if (scrolledPercents > barrier) {
                                scope.percentRiched = true;
                                scope.$broadcast('scrolledDown', [element]);
                            }
                        }
                    });
                }
            };
        };

        return OnScroll;
    });

})(define);