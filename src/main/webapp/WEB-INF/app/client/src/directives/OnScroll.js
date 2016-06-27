/**
 * @author Ruslan Yaniuk
 * @date June 2016
 */
(function (define) {
    "use strict";

    define(function () {

        var OnScroll = function () {
            return {
                restrict: 'A',
                link: function (scope, element) {
                    element.bind('scroll', function () {
                        var target = element[0],
                            leftToScroll = target.scrollHeight - (target.scrollTop + target.clientHeight);

                        scope.$broadcast('containerScrolled', [leftToScroll]);
                    });
                }
            };
        };

        return OnScroll;
    });

})(define);