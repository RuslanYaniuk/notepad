/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var CompareTo = function () {
            return {
                require: "ngModel",
                scope: {
                    otherModelValue: "=compareTo"
                },
                link: function(scope, element, attributes, ngModel) {

                    ngModel.$validators.compareTo = function(modelValue) {
                        return modelValue == scope.otherModelValue;
                    };

                    scope.$watch("otherModelValue", function() {
                        ngModel.$validate();
                    });
                }
            };
        };

        return CompareTo;
    });

})(define);